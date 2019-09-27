package kekmech.ru.core.usecases

import kekmech.ru.core.dto.CoupleNative

interface GetTimetableScheduleUseCase {
    operator fun invoke(dayOfWeek: Int, weekNum: Int): List<CoupleNative>
}