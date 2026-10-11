// Cloudflare Worker для MpeiX Next Web:
//  - /api/*   прокси к ts.mpei.ru (у API нет CORS-заголовков, браузер не может ходить напрямую)
//  - /push/*  Web Push: подписка, очередь сообщений
//  - cron     проверка изменений расписания и отправка push
// Всё остальное отдаётся как статика из ./public (binding ASSETS).

import {
  addDays,
  detectChanges,
  describeChange,
  mapWeek,
  moscowToday,
  startOfWeek,
  toApiDate,
} from '../public/schedule.js';

const UPSTREAM = 'https://ts.mpei.ru/api/';
const WEEKS_TO_CHECK = 3;
const MAX_SUBREQUESTS = 45; // лимит бесплатного плана Workers: 50 на вызов
const PENDING_TTL_SECONDS = 7 * 24 * 60 * 60;

const API_PATH = /^(search|schedule\/(group|person)\/\d+)$/;

export default {
  async fetch(request, env, ctx) {
    const url = new URL(request.url);

    if (url.pathname.startsWith('/api/')) return proxyApi(request, url);
    if (url.pathname.startsWith('/push/')) return handlePush(request, url, env);
    return env.ASSETS.fetch(request);
  },

  async scheduled(_event, env, ctx) {
    ctx.waitUntil(checkAllSchedules(env));
  },
};

// ---------- /api прокси ----------

async function proxyApi(request, url) {
  if (request.method !== 'GET') return json({ error: 'method not allowed' }, 405);
  const path = url.pathname.slice('/api/'.length);
  if (!API_PATH.test(path)) return json({ error: 'not found' }, 404);

  const upstream = await fetch(`${UPSTREAM}${path}${url.search}`, {
    headers: { Accept: 'application/json' },
    cf: { cacheTtl: 120, cacheEverything: true },
  });
  return new Response(upstream.body, {
    status: upstream.status,
    headers: {
      'Content-Type': 'application/json; charset=utf-8',
      'Cache-Control': 'public, max-age=60',
    },
  });
}

// ---------- /push ----------

async function handlePush(request, url, env) {
  const route = url.pathname;

  if (route === '/push/key' && request.method === 'GET') {
    return json({ key: env.VAPID_PUBLIC_KEY ?? null });
  }

  if (route === '/push/subscribe' && request.method === 'POST') {
    const body = await readJson(request);
    const sub = body?.subscription;
    const schedule = body?.schedule;
    if (!isValidSubscription(sub) || !isValidSchedule(schedule)) {
      return json({ error: 'bad request' }, 400);
    }
    const id = await sha256Hex(sub.endpoint);
    await env.SUBS.put(
      `sub:${id}`,
      JSON.stringify({ endpoint: sub.endpoint, schedule, createdAt: Date.now() }),
    );
    return json({ id });
  }

  if (route === '/push/unsubscribe' && request.method === 'POST') {
    const body = await readJson(request);
    if (typeof body?.endpoint !== 'string') return json({ error: 'bad request' }, 400);
    const id = await sha256Hex(body.endpoint);
    await env.SUBS.delete(`sub:${id}`);
    await env.SUBS.delete(`pending:${id}`);
    return json({ ok: true });
  }

  if (route === '/push/pending' && request.method === 'GET') {
    const id = url.searchParams.get('id') ?? '';
    if (!/^[0-9a-f]{64}$/.test(id)) return json({ error: 'bad request' }, 400);
    const raw = await env.SUBS.get(`pending:${id}`);
    if (raw) await env.SUBS.delete(`pending:${id}`);
    return json({ messages: raw ? JSON.parse(raw) : [] });
  }

  return json({ error: 'not found' }, 404);
}

function isValidSubscription(sub) {
  return (
    sub &&
    typeof sub.endpoint === 'string' &&
    sub.endpoint.length <= 1024 &&
    sub.endpoint.startsWith('https://')
  );
}

function isValidSchedule(s) {
  return (
    s &&
    (s.type === 'group' || s.type === 'person') &&
    Number.isInteger(s.id) &&
    s.id > 0 &&
    typeof s.name === 'string' &&
    s.name.length > 0 &&
    s.name.length <= 120
  );
}

async function readJson(request) {
  try {
    const text = await request.text();
    if (text.length > 4096) return null;
    return JSON.parse(text);
  } catch {
    return null;
  }
}

// ---------- проверка расписаний по cron ----------

async function checkAllSchedules(env) {
  const subs = await listSubscriptions(env);
  if (subs.length === 0) return;

  // группируем подписчиков по расписанию
  const bySchedule = new Map();
  for (const sub of subs) {
    const key = `${sub.schedule.type}:${sub.schedule.id}`;
    if (!bySchedule.has(key)) bySchedule.set(key, { schedule: sub.schedule, subs: [] });
    bySchedule.get(key).subs.push(sub);
  }

  // по кругу, чтобы при большом числе расписаний каждое рано или поздно проверялось
  const keys = [...bySchedule.keys()].sort();
  const cursor = Number((await env.SUBS.get('cursor')) ?? 0) % keys.length;
  const ordered = [...keys.slice(cursor), ...keys.slice(0, cursor)];

  let budget = MAX_SUBREQUESTS - 2; // list + cursor
  let processed = 0;
  const today = moscowToday();

  for (const key of ordered) {
    const { schedule, subs: scheduleSubs } = bySchedule.get(key);
    const cost = WEEKS_TO_CHECK + scheduleSubs.length + 2;
    if (cost > budget) continue;
    budget -= cost;
    processed++;

    try {
      await checkSchedule(env, schedule, scheduleSubs, today);
    } catch (e) {
      console.error('checkSchedule failed', key, String(e));
    }
  }

  await env.SUBS.put('cursor', String((cursor + Math.max(processed, 1)) % keys.length));
}

async function listSubscriptions(env) {
  const result = [];
  let cursor;
  do {
    const page = await env.SUBS.list({ prefix: 'sub:', cursor });
    for (const { name } of page.keys) {
      const raw = await env.SUBS.get(name);
      if (!raw) continue;
      try {
        result.push({ id: name.slice('sub:'.length), ...JSON.parse(raw) });
      } catch {
        // повреждённая запись, пропускаем
      }
    }
    cursor = page.list_complete ? undefined : page.cursor;
  } while (cursor && result.length < 500);
  return result;
}

async function checkSchedule(env, schedule, subs, today) {
  const changes = [];
  for (let offset = 0; offset < WEEKS_TO_CHECK; offset++) {
    const monday = addDays(startOfWeek(today), offset * 7);
    const finish = addDays(monday, 6);
    const res = await fetch(
      `${UPSTREAM}schedule/${schedule.type}/${schedule.id}` +
        `?start=${toApiDate(monday)}&finish=${toApiDate(finish)}&lng=1`,
      { headers: { Accept: 'application/json' } },
    );
    if (!res.ok) continue;
    const week = mapWeek(monday, await res.json());

    const snapKey = `snap:${schedule.type}:${schedule.id}:${offset}`;
    const prevRaw = await env.SUBS.get(snapKey);
    const prev = prevRaw ? JSON.parse(prevRaw) : null;
    // сравниваем только одну и ту же неделю (при смене недели offset «съезжает»)
    if (prev && prev.monday === monday) {
      changes.push(...detectChanges(prev, week).filter((c) => c.date >= today));
    }
    await env.SUBS.put(snapKey, JSON.stringify(week), { expirationTtl: 30 * 24 * 60 * 60 });
  }

  if (changes.length === 0) return;

  const message = buildMessage(schedule, changes);
  for (const sub of subs) {
    await env.SUBS.put(`pending:${sub.id}`, JSON.stringify([message]), {
      expirationTtl: PENDING_TTL_SECONDS,
    });
    let status = 0;
    try {
      status = await sendPush(env, sub.endpoint);
    } catch (e) {
      console.warn('push request failed', String(e));
    }
    if (status === 404 || status === 410) {
      await env.SUBS.delete(`sub:${sub.id}`);
      await env.SUBS.delete(`pending:${sub.id}`);
    } else if (status >= 400) {
      console.warn('push failed', status);
    }
  }
}

function buildMessage(schedule, changes) {
  const title =
    changes.length === 1 ? 'Изменение в расписании' : `Изменения в расписании (${changes.length})`;
  const lines = changes
    .sort((a, b) => a.date.localeCompare(b.date))
    .slice(0, 6)
    .map((c) => `${c.date.slice(8, 10)}.${c.date.slice(5, 7)} — ${describeChange(c)}`);
  if (changes.length > 6) lines.push(`… и ещё ${changes.length - 6}`);
  return { title: `${title}: ${schedule.name}`, body: lines.join('\n') };
}

// ---------- Web Push (VAPID, без payload) ----------
// Сообщение без тела: service worker сам забирает текст через /push/pending.
// Благодаря этому не нужно реализовывать шифрование aes128gcm.

let cachedKey;

async function vapidKey(env) {
  if (!cachedKey) {
    const jwk = JSON.parse(env.VAPID_PRIVATE_JWK);
    cachedKey = await crypto.subtle.importKey(
      'jwk',
      jwk,
      { name: 'ECDSA', namedCurve: 'P-256' },
      false,
      ['sign'],
    );
  }
  return cachedKey;
}

async function sendPush(env, endpoint) {
  if (!env.VAPID_PRIVATE_JWK || !env.VAPID_PUBLIC_KEY) return 0;
  const audience = new URL(endpoint).origin;
  const header = b64url(JSON.stringify({ typ: 'JWT', alg: 'ES256' }));
  const payload = b64url(
    JSON.stringify({
      aud: audience,
      exp: Math.floor(Date.now() / 1000) + 12 * 60 * 60,
      sub: env.VAPID_SUBJECT || 'mailto:admin@example.com',
    }),
  );
  const signingInput = `${header}.${payload}`;
  const signature = await crypto.subtle.sign(
    { name: 'ECDSA', hash: 'SHA-256' },
    await vapidKey(env),
    new TextEncoder().encode(signingInput),
  );
  const jwt = `${signingInput}.${b64url(signature)}`;

  const res = await fetch(endpoint, {
    method: 'POST',
    headers: {
      TTL: '86400',
      Urgency: 'high',
      Authorization: `vapid t=${jwt}, k=${env.VAPID_PUBLIC_KEY}`,
      'Content-Length': '0',
    },
  });
  return res.status;
}

// ---------- утилиты ----------

function json(data, status = 200) {
  return new Response(JSON.stringify(data), {
    status,
    headers: { 'Content-Type': 'application/json; charset=utf-8', 'Cache-Control': 'no-store' },
  });
}

async function sha256Hex(text) {
  const digest = await crypto.subtle.digest('SHA-256', new TextEncoder().encode(text));
  return [...new Uint8Array(digest)].map((b) => b.toString(16).padStart(2, '0')).join('');
}

function b64url(input) {
  const bytes = typeof input === 'string' ? new TextEncoder().encode(input) : new Uint8Array(input);
  let bin = '';
  for (const b of bytes) bin += String.fromCharCode(b);
  return btoa(bin).replaceAll('+', '-').replaceAll('/', '_').replace(/=+$/, '');
}
