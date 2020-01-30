package kekmech.ru.core.usecases

import kekmech.ru.core.dto.ScheduleNative

interface GetAllSchedulesUseCase {
    suspend operator fun invoke(): List<ScheduleNative>
}