package kekmech.ru.core.usecases

import kekmech.ru.core.dto.Schedule

/**
 * Сохраняет расписание в БД Android SQLite
 */
interface SaveScheduleUseCase {
    operator fun invoke(schedule: Schedule)
}