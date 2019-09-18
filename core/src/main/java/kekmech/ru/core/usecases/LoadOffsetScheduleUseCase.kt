package kekmech.ru.core.usecases

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Time

interface LoadOffsetScheduleUseCase {
    fun execute(offset: Int, refresh: Boolean): List<CoupleNative>
}