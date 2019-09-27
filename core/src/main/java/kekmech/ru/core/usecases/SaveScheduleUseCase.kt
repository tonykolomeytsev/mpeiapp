package kekmech.ru.core.usecases

import kekmech.ru.core.dto.Schedule

interface SaveScheduleUseCase {
    operator fun invoke(schedule: Schedule)
}