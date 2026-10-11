// Service worker: оффлайн-оболочка и push-уведомления.
const CACHE = 'mpeix-shell-v1';
const SHELL = [
  '/',
  '/index.html',
  '/app.js',
  '/schedule.js',
  '/styles.css',
  '/manifest.webmanifest',
  '/icon.svg',
  '/icon-192.png',
  '/icon-512.png',
  '/apple-touch-icon.png',
];

self.addEventListener('install', (event) => {
  event.waitUntil(
    caches
      .open(CACHE)
      .then((cache) => cache.addAll(SHELL))
      .then(() => self.skipWaiting()),
  );
});

self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches
      .keys()
      .then((keys) => Promise.all(keys.filter((k) => k !== CACHE).map((k) => caches.delete(k))))
      .then(() => self.clients.claim()),
  );
});

self.addEventListener('fetch', (event) => {
  const { request } = event;
  const url = new URL(request.url);
  if (request.method !== 'GET' || url.origin !== location.origin) return;
  // API и push никогда не кэшируем в SW: за оффлайн-данные отвечает приложение
  if (url.pathname.startsWith('/api/') || url.pathname.startsWith('/push/')) return;

  // network-first: пользователь всегда получает свежую версию, оффлайн — кэш
  event.respondWith(
    fetch(request)
      .then((response) => {
        if (response.ok) {
          const copy = response.clone();
          caches.open(CACHE).then((cache) => cache.put(request, copy));
        }
        return response;
      })
      .catch(() =>
        caches
          .match(request)
          .then((hit) => hit ?? caches.match('/index.html')),
      ),
  );
});

async function sha256Hex(text) {
  const digest = await crypto.subtle.digest('SHA-256', new TextEncoder().encode(text));
  return [...new Uint8Array(digest)].map((b) => b.toString(16).padStart(2, '0')).join('');
}

async function showPending() {
  let messages = [];
  try {
    const subscription = await self.registration.pushManager.getSubscription();
    if (subscription) {
      const id = await sha256Hex(subscription.endpoint);
      const res = await fetch(`/push/pending?id=${id}`, { cache: 'no-store' });
      if (res.ok) messages = (await res.json()).messages ?? [];
    }
  } catch {
    // сеть недоступна — покажем общее уведомление ниже
  }

  // iOS требует показывать уведомление на каждый push
  if (messages.length === 0) {
    messages = [{ title: 'Расписание', body: 'В расписании есть изменения. Откройте приложение.' }];
  }
  await Promise.all(
    messages.map((m) =>
      self.registration.showNotification(m.title, {
        body: m.body,
        icon: '/icon-192.png',
        badge: '/icon-192.png',
        tag: 'schedule-changes',
      }),
    ),
  );
}

self.addEventListener('push', (event) => {
  event.waitUntil(showPending());
});

self.addEventListener('notificationclick', (event) => {
  event.notification.close();
  event.waitUntil(
    self.clients.matchAll({ type: 'window', includeUncontrolled: true }).then((clients) => {
      const open = clients.find((c) => 'focus' in c);
      return open ? open.focus() : self.clients.openWindow('/');
    }),
  );
});
