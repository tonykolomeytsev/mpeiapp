package kekmech.ru.core.usecases

import kekmech.ru.core.dto.ScheduleNative

interface GetAllSchedulesUseCase {
    operator fun invoke(): List<ScheduleNative>
}