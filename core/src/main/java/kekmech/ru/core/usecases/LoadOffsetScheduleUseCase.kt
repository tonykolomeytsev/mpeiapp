package kekmech.ru.core.usecases

import kekmech.ru.core.dto.CoupleNative

interface LoadOffsetScheduleUseCase {
    fun execute(offset: Int, refresh: Boolean): List<CoupleNative>
}