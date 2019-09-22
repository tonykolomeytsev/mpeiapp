package kekmech.ru.core.usecases

import kekmech.ru.core.dto.CoupleNative

interface GetTimetableScheduleUseCase {
    fun execute(dayOfWeek: Int, weekNum: Int): List<CoupleNative>
}