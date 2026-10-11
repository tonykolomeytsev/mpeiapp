// Рисует PNG-иконки (те же фигуры, что в public/icon.svg) без внешних зависимостей.
// Запуск: npm run icons
import { deflateSync } from 'node:zlib';
import { writeFileSync } from 'node:fs';
import { fileURLToPath } from 'node:url';
import { dirname, join } from 'node:path';

const OUT = join(dirname(fileURLToPath(import.meta.url)), '..', 'public');

// координаты заданы в системе 512x512
const SHAPES = [
  { x: 0, y: 0, w: 512, h: 512, r: 0, c: [0x25, 0x63, 0xeb] },
  { x: 96, y: 128, w: 320, h: 288, r: 36, c: [255, 255, 255] },
  { x: 96, y: 128, w: 320, h: 76, r: 36, c: [0x1d, 0x4e, 0xd8] },
  { x: 96, y: 168, w: 320, h: 36, r: 0, c: [0x1d, 0x4e, 0xd8] },
  { x: 160, y: 88, w: 28, h: 64, r: 14, c: [255, 255, 255] },
  { x: 324, y: 88, w: 28, h: 64, r: 14, c: [255, 255, 255] },
  ...[
    [140, 240], [230, 240], [320, 240], [140, 312], [230, 312],
  ].map(([x, y]) => ({ x, y, w: 52, h: 40, r: 10, c: [0x25, 0x63, 0xeb] })),
];

function insideRoundedRect(px, py, { x, y, w, h, r }) {
  if (px < x || px >= x + w || py < y || py >= y + h) return false;
  const cx = px < x + r ? x + r : px >= x + w - r ? x + w - r : px;
  const cy = py < y + r ? y + r : py >= y + h - r ? y + h - r : py;
  return (px - cx) ** 2 + (py - cy) ** 2 <= r * r;
}

function render(size) {
  const scale = 512 / size;
  const SS = 2; // суперсэмплинг 2x2 для сглаживания
  const raw = Buffer.alloc((size * 4 + 1) * size);
  for (let j = 0; j < size; j++) {
    raw[j * (size * 4 + 1)] = 0; // filter: none
    for (let i = 0; i < size; i++) {
      let r = 0, g = 0, b = 0;
      for (let sj = 0; sj < SS; sj++) {
        for (let si = 0; si < SS; si++) {
          const px = (i + (si + 0.5) / SS) * scale;
          const py = (j + (sj + 0.5) / SS) * scale;
          let color = SHAPES[0].c;
          for (const shape of SHAPES) if (insideRoundedRect(px, py, shape)) color = shape.c;
          r += color[0]; g += color[1]; b += color[2];
        }
      }
      const n = SS * SS;
      const o = j * (size * 4 + 1) + 1 + i * 4;
      raw[o] = Math.round(r / n);
      raw[o + 1] = Math.round(g / n);
      raw[o + 2] = Math.round(b / n);
      raw[o + 3] = 255;
    }
  }
  return encodePng(size, size, raw);
}

const crcTable = Array.from({ length: 256 }, (_, n) => {
  let c = n;
  for (let k = 0; k < 8; k++) c = c & 1 ? 0xedb88320 ^ (c >>> 1) : c >>> 1;
  return c >>> 0;
});

function crc32(buf) {
  let c = 0xffffffff;
  for (const byte of buf) c = crcTable[(c ^ byte) & 0xff] ^ (c >>> 8);
  return (c ^ 0xffffffff) >>> 0;
}

function chunk(type, data) {
  const len = Buffer.alloc(4);
  len.writeUInt32BE(data.length);
  const body = Buffer.concat([Buffer.from(type, 'ascii'), data]);
  const crc = Buffer.alloc(4);
  crc.writeUInt32BE(crc32(body));
  return Buffer.concat([len, body, crc]);
}

function encodePng(width, height, raw) {
  const ihdr = Buffer.alloc(13);
  ihdr.writeUInt32BE(width, 0);
  ihdr.writeUInt32BE(height, 4);
  ihdr[8] = 8; // bit depth
  ihdr[9] = 6; // RGBA
  return Buffer.concat([
    Buffer.from([0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a]),
    chunk('IHDR', ihdr),
    chunk('IDAT', deflateSync(raw, { level: 9 })),
    chunk('IEND', Buffer.alloc(0)),
  ]);
}

for (const [name, size] of [
  ['icon-512.png', 512],
  ['icon-192.png', 192],
  ['apple-touch-icon.png', 180],
]) {
  writeFileSync(join(OUT, name), render(size));
  console.log('written', name);
}
