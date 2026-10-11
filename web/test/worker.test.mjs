import test from 'node:test';
import assert from 'node:assert/strict';
import { webcrypto as crypto } from 'node:crypto';
import worker from '../worker/index.js';
import { addDays, moscowToday, startOfWeek, toApiDate } from '../public/schedule.js';

class FakeKV {
  constructor() { this.map = new Map(); }
  async get(key) { return this.map.get(key) ?? null; }
  async put(key, value) { this.map.set(key, value); }
  async delete(key) { this.map.delete(key); }
  async list({ prefix = '' } = {}) {
    const keys = [...this.map.keys()].filter((k) => k.startsWith(prefix)).map((name) => ({ name }));
    return { keys, list_complete: true };
  }
}

async function makeEnv() {
  const { publicKey, privateKey } = await crypto.subtle.generateKey(
    { name: 'ECDSA', namedCurve: 'P-256' }, true, ['sign', 'verify'],
  );
  return {
    env: {
      SUBS: new FakeKV(),
      VAPID_SUBJECT: 'mailto:test@example.com',
      VAPID_PUBLIC_KEY: Buffer.from(await crypto.subtle.exportKey('raw', publicKey)).toString('base64url'),
      VAPID_PRIVATE_JWK: JSON.stringify(await crypto.subtle.exportKey('jwk', privateKey)),
      ASSETS: { fetch: async () => new Response('asset') },
    },
    publicKey,
  };
}

function lesson(over = {}) {
  return {
    date: toApiDate(moscowToday()), // сегодня, чтобы изменение не отфильтровалось как прошедшее
    discipline: 'Математика',
    kindOfWork: 'Лекция',
    auditorium: 'Е-101',
    lecturer: 'Иванов И.И.',
    beginLesson: '09:20',
    endLesson: '10:55',
    lessonNumberStart: 1,
    ...over,
  };
}

async function runCron(env) {
  const tasks = [];
  await worker.scheduled({}, env, { waitUntil: (p) => tasks.push(p) });
  await Promise.all(tasks);
}

async function call(env, path, init) {
  return worker.fetch(new Request(`https://x.test${path}`, init), env, {});
}

test('прокси пропускает только известные пути', async () => {
  const { env } = await makeEnv();
  const res = await call(env, '/api/admin/secret');
  assert.equal(res.status, 404);
  const post = await call(env, '/api/search?term=a', { method: 'POST' });
  assert.equal(post.status, 405);
});

test('подписка валидируется и pending отдаётся один раз', async () => {
  const { env } = await makeEnv();
  const bad = await call(env, '/push/subscribe', {
    method: 'POST',
    body: JSON.stringify({ subscription: { endpoint: 'http://insecure' }, schedule: {} }),
  });
  assert.equal(bad.status, 400);

  const ok = await call(env, '/push/subscribe', {
    method: 'POST',
    body: JSON.stringify({
      subscription: { endpoint: 'https://push.test/abc' },
      schedule: { type: 'group', id: 20949, name: 'ЭЛ-01-23' },
    }),
  });
  assert.equal(ok.status, 200);
  const { id } = await ok.json();
  assert.match(id, /^[0-9a-f]{64}$/);

  await env.SUBS.put(`pending:${id}`, JSON.stringify([{ title: 't', body: 'b' }]));
  const first = await (await call(env, `/push/pending?id=${id}`)).json();
  const second = await (await call(env, `/push/pending?id=${id}`)).json();
  assert.equal(first.messages.length, 1);
  assert.equal(second.messages.length, 0);
});

test('cron: первый запуск делает снимок, второй находит изменение и шлёт push с валидной VAPID-подписью', async () => {
  const { env, publicKey } = await makeEnv();
  await call(env, '/push/subscribe', {
    method: 'POST',
    body: JSON.stringify({
      subscription: { endpoint: 'https://push.test/device-1' },
      schedule: { type: 'group', id: 20949, name: 'ЭЛ-01-23' },
    }),
  });

  let room = 'Е-101';
  const pushes = [];
  const realFetch = globalThis.fetch;
  globalThis.fetch = async (url, init = {}) => {
    const u = String(url);
    if (u.startsWith('https://ts.mpei.ru/api/schedule/group/20949')) {
      const monday = startOfWeek(moscowToday());
      const start = new URL(u).searchParams.get('start');
      return Response.json(start === toApiDate(monday) ? [lesson({ auditorium: room })] : []);
    }
    if (u.startsWith('https://push.test/')) {
      pushes.push({ url: u, headers: init.headers });
      return new Response(null, { status: 201 });
    }
    throw new Error(`unexpected fetch ${u}`);
  };

  try {
    await runCron(env);
    await runCron(env); // ничего не менялось
    assert.equal(pushes.length, 0, 'без изменений push не отправляется');

    room = 'Е-205';
    await runCron(env);
  } finally {
    globalThis.fetch = realFetch;
  }

  assert.equal(pushes.length, 1);
  const auth = pushes[0].headers.Authorization;
  const [, jwt] = /t=([^,]+),/.exec(auth);
  const [h, p, s] = jwt.split('.');
  const valid = await crypto.subtle.verify(
    { name: 'ECDSA', hash: 'SHA-256' },
    publicKey,
    Buffer.from(s, 'base64url'),
    new TextEncoder().encode(`${h}.${p}`),
  );
  assert.equal(valid, true, 'JWT подписан приватным ключом VAPID');
  const claims = JSON.parse(Buffer.from(p, 'base64url').toString());
  assert.equal(claims.aud, 'https://push.test');

  const [subId] = [...env.SUBS.map.keys()].filter((k) => k.startsWith('pending:'));
  const pending = JSON.parse(env.SUBS.map.get(subId));
  assert.match(pending[0].body, /аудитория \(Е-101 → Е-205\)/);
});

test('cron: 410 от push-сервиса удаляет подписку', async () => {
  const { env } = await makeEnv();
  await call(env, '/push/subscribe', {
    method: 'POST',
    body: JSON.stringify({
      subscription: { endpoint: 'https://push.test/gone' },
      schedule: { type: 'person', id: 5727, name: 'Сорокина Я.А.' },
    }),
  });
  let name = 'Физика';
  const realFetch = globalThis.fetch;
  globalThis.fetch = async (url) => {
    const u = String(url);
    if (u.includes('/schedule/person/5727')) {
      return Response.json(
        u.includes(toApiDate(startOfWeek(moscowToday())))
          ? [lesson({ discipline: name })]
          : [],
      );
    }
    return new Response(null, { status: 410 });
  };
  try {
    await runCron(env);
    name = 'Химия';
    await runCron(env);
  } finally {
    globalThis.fetch = realFetch;
  }
  assert.equal([...env.SUBS.map.keys()].some((k) => k.startsWith('sub:')), false);
  void addDays;
});
