// Чистая логика расписания (без DOM): используется и в браузере, и в Cloudflare Worker.
// Порт Kotlin-кода MpeiScheduleMapper / ScheduleChangeDetector из Android-приложения.

const DAY_MS = 24 * 60 * 60 * 1000;

/** Даты везде хранятся строкой YYYY-MM-DD, арифметика через UTC, чтобы не зависеть от часового пояса. */
function parse(dateStr) {
  const [y, m, d] = dateStr.split('-').map(Number);
  return new Date(Date.UTC(y, m - 1, d));
}

function format(date) {
  return date.toISOString().slice(0, 10);
}

export function addDays(dateStr, days) {
  return format(new Date(parse(dateStr).getTime() + days * DAY_MS));
}

/** Текущая дата в Москве (расписание МЭИ привязано к московскому времени) */
export function moscowToday(now = new Date()) {
  return new Intl.DateTimeFormat('en-CA', {
    timeZone: 'Europe/Moscow',
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
  }).format(now);
}

export function startOfWeek(dateStr) {
  const dow = parse(dateStr).getUTCDay(); // 0 = Вс
  const shift = dow === 0 ? 6 : dow - 1;
  return addDays(dateStr, -shift);
}

/** 2026-10-12 -> 2026.10.12 (формат API ts.mpei.ru) */
export function toApiDate(dateStr) {
  return dateStr.replaceAll('-', '.');
}

function fromApiDate(apiDate) {
  return /^\d{4}\.\d{2}\.\d{2}$/.test(apiDate ?? '') ? apiDate.replaceAll('.', '-') : null;
}

const LESSON_STARTS = {
  '09:20': 1,
  '11:10': 2,
  '13:45': 3,
  '15:35': 4,
  '17:20': 5,
  '18:55': 6,
  '20:30': 7,
};

export const CLASSES_TYPES = {
  LECTURE: 'LECTURE',
  PRACTICE: 'PRACTICE',
  LAB: 'LAB',
  COURSE: 'COURSE',
  EXAM: 'EXAM',
  CONSULTATION: 'CONSULTATION',
  UNDEFINED: 'UNDEFINED',
};

export function mapClassesType(rawType = '') {
  const lower = rawType.toLowerCase();
  if (lower.includes('лек')) return CLASSES_TYPES.LECTURE;
  if (lower.includes('лаб')) return CLASSES_TYPES.LAB;
  if (lower.includes('прак')) return CLASSES_TYPES.PRACTICE;
  if (lower.includes('курс') || lower.includes('кп')) return CLASSES_TYPES.COURSE;
  if (lower.includes('экз')) return CLASSES_TYPES.EXAM;
  if (lower.includes('конс')) return CLASSES_TYPES.CONSULTATION;
  return CLASSES_TYPES.UNDEFINED;
}

/** Номер недели семестра, null если вне 1..18 (как в Android-приложении) */
export function weekOfSemester(mondayStr) {
  const monday = parse(mondayStr);
  const month = monday.getUTCMonth() + 1;
  let startMonday;
  if (month >= 9 || month === 1) {
    const year = month === 1 ? monday.getUTCFullYear() - 1 : monday.getUTCFullYear();
    const sept1 = `${year}-09-01`;
    startMonday = parse(sept1).getUTCDay() === 0 ? addDays(sept1, 1) : startOfWeek(sept1);
  } else {
    let feb1 = `${monday.getUTCFullYear()}-02-01`;
    while (parse(feb1).getUTCDay() !== 1) feb1 = addDays(feb1, 1);
    startMonday = feb1;
  }
  const diffWeeks = Math.trunc((monday - parse(startMonday)) / (7 * DAY_MS));
  const num = 1 + diffWeeks;
  return num >= 1 && num <= 18 ? num : null;
}

/**
 * Преобразует ответ /schedule/{type}/{id} в модель недели:
 * { monday, weekOfSemester, days: [{ date, dayOfWeek, classes: [...] }] }
 */
export function mapWeek(mondayStr, rawClasses) {
  const byDate = new Map();
  for (const cls of rawClasses ?? []) {
    const date = fromApiDate(cls.date);
    if (!date) continue;
    const start = cls.beginLesson ?? '09:20';
    const end = cls.endLesson ?? '10:55';
    const lecturer = (cls.lecturer ?? '').trim();
    const item = {
      name: cls.discipline ?? '',
      type: mapClassesType(cls.kindOfWork ?? ''),
      rawType: cls.kindOfWork ?? null,
      place: cls.auditorium ?? '',
      groups: cls.stream ?? cls.group ?? cls.subGroup ?? '',
      person: lecturer.toLowerCase().includes('вакансия') ? '' : lecturer,
      start,
      end,
      number: cls.lessonNumberStart ?? LESSON_STARTS[start] ?? -1,
    };
    if (!byDate.has(date)) byDate.set(date, []);
    byDate.get(date).push(item);
  }

  const days = [];
  for (let i = 0; i < 7; i++) {
    const date = addDays(mondayStr, i);
    const classes = (byDate.get(date) ?? []).sort((a, b) => a.start.localeCompare(b.start));
    days.push({ date, dayOfWeek: i + 1, classes });
  }
  return { monday: mondayStr, weekOfSemester: weekOfSemester(mondayStr), days };
}

// ---------- Детектор изменений ----------

export const CHANGE_TYPES = {
  ADDED: 'ADDED',
  REMOVED: 'REMOVED',
  MODIFIED: 'MODIFIED',
  REPLACED: 'REPLACED',
};

function sameClasses(a, b) {
  return (
    a.name === b.name &&
    a.type === b.type &&
    a.place === b.place &&
    a.person === b.person &&
    a.start === b.start &&
    a.end === b.end &&
    a.groups === b.groups
  );
}

/** Сравнивает две недели (формат mapWeek) и возвращает список изменений */
export function detectChanges(oldWeek, newWeek) {
  if (!oldWeek || !newWeek) return [];
  const changes = [];
  for (const newDay of newWeek.days) {
    const oldDay = oldWeek.days.find((d) => d.date === newDay.date);
    if (!oldDay) continue;

    for (const oldClass of oldDay.classes) {
      const match = newDay.classes.find((c) => c.number === oldClass.number);
      if (!match) {
        changes.push({ type: CHANGE_TYPES.REMOVED, date: newDay.date, oldClass, newClass: null });
      } else if (!sameClasses(oldClass, match)) {
        const type = oldClass.name !== match.name ? CHANGE_TYPES.REPLACED : CHANGE_TYPES.MODIFIED;
        changes.push({ type, date: newDay.date, oldClass, newClass: match });
      }
    }
    for (const newClass of newDay.classes) {
      if (!oldDay.classes.some((c) => c.number === newClass.number)) {
        changes.push({ type: CHANGE_TYPES.ADDED, date: newDay.date, oldClass: null, newClass });
      }
    }
  }
  return changes;
}

export function describeChange(change) {
  const { type, oldClass, newClass } = change;
  switch (type) {
    case CHANGE_TYPES.ADDED:
      return `Добавлена пара: ${newClass.name}`;
    case CHANGE_TYPES.REMOVED:
      return `Отменена пара: ${oldClass.name}`;
    case CHANGE_TYPES.REPLACED:
      return `Заменена пара: ${oldClass.name} → ${newClass.name}`;
    default: {
      const parts = [];
      if (oldClass.start !== newClass.start || oldClass.end !== newClass.end) {
        parts.push(`время (${oldClass.start} → ${newClass.start})`);
      }
      if (oldClass.place !== newClass.place) {
        parts.push(`аудитория (${oldClass.place || '—'} → ${newClass.place || '—'})`);
      }
      if (oldClass.person !== newClass.person) parts.push('преподаватель');
      if (oldClass.type !== newClass.type) parts.push('тип');
      if (oldClass.groups !== newClass.groups) parts.push('группы');
      return `Изменена пара ${newClass.name}: ${parts.join(', ')}`;
    }
  }
}
