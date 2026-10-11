import test from 'node:test';
import assert from 'node:assert/strict';
import {
  addDays,
  startOfWeek,
  weekOfSemester,
  mapWeek,
  mapClassesType,
  detectChanges,
  describeChange,
  CHANGE_TYPES,
} from '../public/schedule.js';

const cls = (over = {}) => ({
  date: '2026.10.12',
  discipline: 'Математика',
  kindOfWork: 'Лекция',
  auditorium: 'Е-101',
  lecturer: 'Иванов И.И.',
  group: 'ЭЛ-01-23',
  beginLesson: '09:20',
  endLesson: '10:55',
  ...over,
});

test('startOfWeek / addDays', () => {
  assert.equal(startOfWeek('2026-10-11'), '2026-10-05'); // воскресенье
  assert.equal(startOfWeek('2026-10-12'), '2026-10-12'); // понедельник
  assert.equal(addDays('2026-12-30', 3), '2027-01-02');
});

test('weekOfSemester: осенний и весенний семестр', () => {
  // 1 сентября 2026 — вторник, первая неделя начинается 2026-08-31
  assert.equal(weekOfSemester('2026-08-31'), null); // месяц 8 -> весенняя ветка, вне диапазона
  assert.equal(weekOfSemester('2026-09-07'), 2);
  assert.equal(weekOfSemester('2026-10-12'), 7);
  assert.equal(weekOfSemester('2027-02-08'), 2); // 1 февраля 2027 — понедельник
  assert.equal(weekOfSemester('2026-07-06'), null);
});

test('mapClassesType', () => {
  assert.equal(mapClassesType('Лекция'), 'LECTURE');
  assert.equal(mapClassesType('Лабораторная работа'), 'LAB');
  assert.equal(mapClassesType('Практическое занятие'), 'PRACTICE');
  assert.equal(mapClassesType('Экзамен'), 'EXAM');
  assert.equal(mapClassesType('что-то'), 'UNDEFINED');
});

test('mapWeek: 7 дней, номер пары по времени, вакансия скрывается', () => {
  const week = mapWeek('2026-10-12', [
    cls({ beginLesson: '11:10', endLesson: '12:45', discipline: 'Физика' }),
    cls({ lecturer: 'вакансия' }),
    cls({ date: 'мусор' }),
  ]);
  assert.equal(week.days.length, 7);
  const monday = week.days[0];
  assert.equal(monday.classes.length, 2);
  assert.deepEqual(monday.classes.map((c) => c.number), [1, 2]);
  assert.equal(monday.classes[0].person, '');
  assert.equal(week.days[1].classes.length, 0);
});

test('detectChanges: добавление, отмена, замена, изменение', () => {
  const old = mapWeek('2026-10-12', [
    cls({ lessonNumberStart: 1 }),
    cls({ lessonNumberStart: 2, beginLesson: '11:10', endLesson: '12:45', discipline: 'Физика' }),
    cls({ lessonNumberStart: 3, beginLesson: '13:45', endLesson: '15:20', discipline: 'Химия' }),
  ]);
  const next = mapWeek('2026-10-12', [
    cls({ lessonNumberStart: 1, auditorium: 'Е-205' }), // изменена аудитория
    cls({ lessonNumberStart: 2, beginLesson: '11:10', endLesson: '12:45', discipline: 'Информатика' }), // замена
    // 3-я пара отменена
    cls({ lessonNumberStart: 4, beginLesson: '15:35', endLesson: '17:10', discipline: 'ОБЖ' }), // добавлена
  ]);
  const changes = detectChanges(old, next);
  const types = changes.map((c) => c.type).sort();
  assert.deepEqual(types, [
    CHANGE_TYPES.ADDED,
    CHANGE_TYPES.MODIFIED,
    CHANGE_TYPES.REMOVED,
    CHANGE_TYPES.REPLACED,
  ].sort());
  const modified = changes.find((c) => c.type === CHANGE_TYPES.MODIFIED);
  assert.match(describeChange(modified), /аудитория \(Е-101 → Е-205\)/);
});

test('detectChanges: без старой недели изменений нет', () => {
  assert.deepEqual(detectChanges(null, mapWeek('2026-10-12', [cls()])), []);
});
