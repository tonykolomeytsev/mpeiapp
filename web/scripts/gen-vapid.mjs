// Генерирует пару VAPID-ключей для Web Push.
// Запуск: npm run vapid
import { webcrypto as crypto } from 'node:crypto';

const { publicKey, privateKey } = await crypto.subtle.generateKey(
  { name: 'ECDSA', namedCurve: 'P-256' },
  true,
  ['sign', 'verify'],
);

const b64url = (buf) => Buffer.from(buf).toString('base64url');
const rawPublic = await crypto.subtle.exportKey('raw', publicKey);
const privateJwk = await crypto.subtle.exportKey('jwk', privateKey);

console.log('VAPID_PUBLIC_KEY (вставьте в wrangler.toml -> [vars]):');
console.log(b64url(rawPublic));
console.log('\nVAPID_PRIVATE_JWK (команда: npx wrangler secret put VAPID_PRIVATE_JWK, вставьте строку):');
console.log(JSON.stringify(privateJwk));
