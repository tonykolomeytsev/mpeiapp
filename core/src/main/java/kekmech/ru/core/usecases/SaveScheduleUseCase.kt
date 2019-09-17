package kekmech.ru.core.usecases

import kekmech.ru.core.dto.Schedule

interface SaveScheduleUseCase {
    fun save(schedule: Schedule)
}