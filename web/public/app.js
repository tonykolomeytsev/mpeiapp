import { addDays, mapWeek, moscowToday, startOfWeek, toApiDate, weekOfSemester } from './schedule.js';

const $ = (id) => document.getElementById(id);
const DOW = ['Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб', 'Вс'];
const MONTHS = ['янв', 'фев', 'мар', 'апр', 'май', 'июн', 'июл', 'авг', 'сен', 'окт', 'ноя', 'дек'];
const TYPE_LABEL = {
  LECTURE: 'Лекция',
  PRACTICE: 'Практика',
  LAB: 'Лабораторная',
  COURSE: 'Курсовая',
  EXAM: 'Экзамен',
  CONSULTATION: 'Консультация',
};
const LESSON_END_GRACE = 0;

const store = {
  get(key, fallback = null) {
    try {
      const raw = localStorage.getItem(key);
      return raw === null ? fallback : JSON.parse(raw);
    } catch {
      return fallback;
    }
  },
  set(key, value) {
    try {
      localStorage.setItem(key, JSON.stringify(value));
    } catch {
      // хранилище недоступно (приватный режим / переполнено)
    }
  },
};

const state = {
  schedule: store.get('mpeix.schedule'), // { type, id, name }
  weekOffset: 0,
  selectedDate: null,
  week: null,
  requestId: 0,
};

// ---------- API ----------

async function api(path) {
  const controller = new AbortController();
  const timer = setTimeout(() => controller.abort(), 12000);
  try {
    const res = await fetch(`/api/${path}`, { signal: controller.signal });
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    return await res.json();
  } finally {
    clearTimeout(timer);
  }
}

function cacheKey(schedule, monday) {
  return `mpeix.week.${schedule.type}.${schedule.id}.${monday}`;
}

async function loadWeek(schedule, monday) {
  const finish = addDays(monday, 6);
  try {
    const raw = await api(
      `schedule/${schedule.type}/${schedule.id}?start=${toApiDate(monday)}&finish=${toApiDate(finish)}&lng=1`,
    );
    const week = mapWeek(monday, raw);
    store.set(cacheKey(schedule, monday), { week, savedAt: Date.now() });
    return { week, offline: false };
  } catch (error) {
    const cached = store.get(cacheKey(schedule, monday));
    if (cached) return { week: cached.week, offline: true, savedAt: cached.savedAt };
    throw error;
  }
}

// ---------- Отрисовка ----------

function el(tag, props = {}, ...children) {
  const node = document.createElement(tag);
  for (const [key, value] of Object.entries(props)) {
    if (key === 'class') node.className = value;
    else if (key.startsWith('on')) node.addEventListener(key.slice(2), value);
    else if (value !== false && value != null) node.setAttribute(key, value);
  }
  for (const child of children.flat()) {
    if (child == null || child === false) continue;
    node.append(child.nodeType ? child : document.createTextNode(String(child)));
  }
  return node;
}

function shortDate(dateStr) {
  const [, m, d] = dateStr.split('-').map(Number);
  return `${d} ${MONTHS[m - 1]}`;
}

function minutes(hhmm) {
  const [h, m] = hhmm.split(':').map(Number);
  return h * 60 + m;
}

function nowInMoscow() {
  const parts = new Intl.DateTimeFormat('en-GB', {
    timeZone: 'Europe/Moscow',
    hour: '2-digit',
    minute: '2-digit',
    hour12: false,
  }).format(new Date());
  return minutes(parts);
}

function setStatus(text) {
  const box = $('status');
  box.hidden = !text;
  box.textContent = text ?? '';
}

function render() {
  $('title').textContent = state.schedule?.name ?? 'Выберите группу';
  const today = moscowToday();
  const monday = addDays(startOfWeek(today), state.weekOffset * 7);
  const sunday = addDays(monday, 6);
  const sem = weekOfSemester(monday);

  $('week-title').textContent = `${shortDate(monday)} – ${shortDate(sunday)}`;
  $('week-sub').textContent =
    (sem ? `${sem} неделя` : 'Вне семестра') + (state.weekOffset === 0 ? ' · сейчас' : '');

  const days = $('days');
  days.replaceChildren(
    ...Array.from({ length: 7 }, (_, i) => {
      const date = addDays(monday, i);
      const day = state.week?.days[i];
      const classes = ['day'];
      if (date === today) classes.push('today');
      if (day?.classes.length) classes.push('has');
      return el(
        'button',
        {
          class: classes.join(' '),
          type: 'button',
          role: 'tab',
          'aria-selected': String(date === state.selectedDate),
          onclick: () => {
            state.selectedDate = date;
            render();
          },
        },
        el('span', { class: 'dow' }, DOW[i]),
        el('span', { class: 'num' }, Number(date.slice(8))),
      );
    }),
  );

  const content = $('content');
  if (!state.schedule) {
    content.replaceChildren(
      el('div', { class: 'empty' }, el('div', { class: 'big' }, '🎓'), el('p', {}, 'Выберите группу или преподавателя')),
    );
    return;
  }
  if (!state.week) {
    content.replaceChildren(el('div', { class: 'empty' }, 'Загрузка…'));
    return;
  }

  const day = state.week.days.find((d) => d.date === state.selectedDate) ?? state.week.days[0];
  if (day.classes.length === 0) {
    content.replaceChildren(
      el('div', { class: 'empty' }, el('div', { class: 'big' }, '🌴'), el('p', {}, 'В этот день занятий нет')),
    );
    return;
  }

  const isToday = day.date === today;
  const nowMin = nowInMoscow();
  content.replaceChildren(
    ...day.classes.map((c) => {
      const current =
        isToday && nowMin >= minutes(c.start) && nowMin < minutes(c.end) + LESSON_END_GRACE;
      return el(
        'article',
        { class: `card ${c.type}${current ? ' now' : ''}` },
        el(
          'div',
          { class: 'time' },
          el('span', {}, `${c.start} – ${c.end}`),
          el('span', {}, c.number > 0 ? `${c.number} пара` : ''),
        ),
        el('div', { class: 'name' }, c.name),
        el('div', { class: 'meta' }, [c.place, c.person].filter(Boolean).join(' · ')),
        c.groups ? el('div', { class: 'meta' }, c.groups) : null,
        TYPE_LABEL[c.type] || c.rawType
          ? el('span', { class: 'badge' }, TYPE_LABEL[c.type] ?? c.rawType)
          : null,
      );
    }),
  );
}

async function refresh() {
  if (!state.schedule) {
    state.week = null;
    render();
    return;
  }
  const requestId = ++state.requestId;
  const monday = addDays(startOfWeek(moscowToday()), state.weekOffset * 7);
  const cached = store.get(cacheKey(state.schedule, monday));
  state.week = cached?.week ?? null;
  pickSelectedDate(monday);
  render();
  setStatus(null);

  try {
    const { week, offline, savedAt } = await loadWeek(state.schedule, monday);
    if (requestId !== state.requestId) return;
    state.week = week;
    setStatus(
      offline
        ? `Нет связи. Показаны сохранённые данные от ${new Date(savedAt).toLocaleString('ru-RU')}`
        : null,
    );
  } catch {
    if (requestId !== state.requestId) return;
    setStatus('Не удалось загрузить расписание. Проверьте подключение.');
  }
  render();
}

function pickSelectedDate(monday) {
  const today = moscowToday();
  const inWeek = (d) => d >= monday && d <= addDays(monday, 6);
  if (state.selectedDate && inWeek(state.selectedDate)) return;
  state.selectedDate = inWeek(today) ? today : monday;
}

// ---------- Поиск ----------

let searchType = 'group';
let searchTimer;

function openSearch() {
  const dialog = $('dlg-search');
  $('search-input').value = '';
  $('search-results').replaceChildren();
  dialog.showModal();
  $('search-input').focus();
}

function updateSegments() {
  $('seg-group').classList.toggle('on', searchType === 'group');
  $('seg-person').classList.toggle('on', searchType === 'person');
  $('search-input').placeholder = searchType === 'group' ? 'Например: ЭЛ-01-23' : 'Например: Сорокин';
}

async function runSearch() {
  const term = $('search-input').value.trim();
  const list = $('search-results');
  if (term.length < 2) {
    list.replaceChildren();
    return;
  }
  try {
    const results = await api(`search?term=${encodeURIComponent(term)}&type=${searchType}`);
    list.replaceChildren(
      ...results.slice(0, 30).map((r) =>
        el(
          'li',
          {},
          el(
            'button',
            { type: 'button', onclick: () => selectSchedule({ type: r.type, id: r.id, name: r.label }) },
            r.label,
            el('small', {}, r.description ?? ''),
          ),
        ),
      ),
    );
    if (results.length === 0) list.replaceChildren(el('li', { class: 'hint' }, 'Ничего не найдено'));
  } catch {
    list.replaceChildren(el('li', { class: 'hint' }, 'Ошибка поиска. Проверьте подключение.'));
  }
}

async function selectSchedule(schedule) {
  state.schedule = schedule;
  store.set('mpeix.schedule', schedule);
  state.weekOffset = 0;
  state.selectedDate = null;
  $('dlg-search').close();
  await refresh();
  syncPushSchedule();
}

// ---------- Push ----------

const pushSupported =
  'serviceWorker' in navigator && 'PushManager' in window && 'Notification' in window;

function isStandalone() {
  return window.matchMedia('(display-mode: standalone)').matches || navigator.standalone === true;
}

function isIos() {
  return /iphone|ipad|ipod/i.test(navigator.userAgent) ||
    (navigator.platform === 'MacIntel' && navigator.maxTouchPoints > 1);
}

function urlBase64ToUint8Array(base64) {
  const padded = base64 + '='.repeat((4 - (base64.length % 4)) % 4);
  const raw = atob(padded.replaceAll('-', '+').replaceAll('_', '/'));
  return Uint8Array.from(raw, (c) => c.charCodeAt(0));
}

async function currentSubscription() {
  if (!pushSupported) return null;
  const reg = await navigator.serviceWorker.ready;
  return reg.pushManager.getSubscription();
}

async function postJson(url, body) {
  const res = await fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  });
  if (!res.ok) throw new Error(`HTTP ${res.status}`);
  return res.json();
}

async function enablePush() {
  if (!state.schedule) throw new Error('Сначала выберите группу или преподавателя');
  const permission = await Notification.requestPermission();
  if (permission !== 'granted') throw new Error('Разрешите уведомления в настройках устройства');

  const { key } = await (await fetch('/push/key')).json();
  if (!key) throw new Error('Сервер не настроен для уведомлений');

  const reg = await navigator.serviceWorker.ready;
  const subscription =
    (await reg.pushManager.getSubscription()) ??
    (await reg.pushManager.subscribe({
      userVisibleOnly: true,
      applicationServerKey: urlBase64ToUint8Array(key),
    }));
  await postJson('/push/subscribe', {
    subscription: subscription.toJSON(),
    schedule: state.schedule,
  });
}

async function disablePush() {
  const subscription = await currentSubscription();
  if (!subscription) return;
  const endpoint = subscription.endpoint;
  await subscription.unsubscribe();
  await postJson('/push/unsubscribe', { endpoint }).catch(() => {});
}

/** Если пользователь сменил группу, обновляем её на сервере */
async function syncPushSchedule() {
  try {
    const subscription = await currentSubscription();
    if (subscription && state.schedule) {
      await postJson('/push/subscribe', { subscription: subscription.toJSON(), schedule: state.schedule });
    }
  } catch {
    // не критично
  }
}

async function openSettings() {
  const toggle = $('push-toggle');
  const hint = $('install-hint');
  hint.hidden = true;
  toggle.disabled = false;

  if (!pushSupported) {
    toggle.checked = false;
    toggle.disabled = true;
    hint.hidden = false;
    hint.textContent = isIos() && !isStandalone()
      ? 'Чтобы включить уведомления на iPhone: нажмите «Поделиться» в Safari → «На экран „Домой“», затем откройте MpeiX с главного экрана.'
      : 'Этот браузер не поддерживает push-уведомления.';
  } else {
    toggle.checked =
      Notification.permission === 'granted' && Boolean(await currentSubscription());
  }
  $('dlg-settings').showModal();
}

// ---------- Инициализация ----------

function bind() {
  $('btn-pick').addEventListener('click', openSearch);
  $('btn-settings').addEventListener('click', openSettings);
  $('btn-prev').addEventListener('click', () => shiftWeek(-1));
  $('btn-next').addEventListener('click', () => shiftWeek(1));
  $('btn-today').addEventListener('click', () => {
    state.weekOffset = 0;
    state.selectedDate = null;
    refresh();
  });

  $('seg-group').addEventListener('click', () => {
    searchType = 'group';
    updateSegments();
    runSearch();
  });
  $('seg-person').addEventListener('click', () => {
    searchType = 'person';
    updateSegments();
    runSearch();
  });
  $('search-input').addEventListener('input', () => {
    clearTimeout(searchTimer);
    searchTimer = setTimeout(runSearch, 300);
  });

  $('push-toggle').addEventListener('change', async (event) => {
    const toggle = event.target;
    toggle.disabled = true;
    try {
      if (toggle.checked) await enablePush();
      else await disablePush();
    } catch (error) {
      toggle.checked = !toggle.checked;
      alert(error.message || 'Не удалось изменить настройку уведомлений');
    } finally {
      toggle.disabled = false;
    }
  });

  // клик по подложке закрывает диалог
  for (const dialog of document.querySelectorAll('dialog')) {
    dialog.addEventListener('click', (event) => {
      if (event.target === dialog) dialog.close();
    });
  }

  document.addEventListener('visibilitychange', () => {
    if (document.visibilityState === 'visible') refresh();
  });
}

function shiftWeek(delta) {
  state.weekOffset += delta;
  state.selectedDate = null;
  refresh();
}

function registerServiceWorker() {
  if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('/sw.js').catch(() => {});
  }
}

bind();
updateSegments();
registerServiceWorker();
refresh();
if (!state.schedule) openSearch();
