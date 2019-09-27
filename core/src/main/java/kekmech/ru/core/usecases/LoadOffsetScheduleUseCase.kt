package kekmech.ru.core.usecases

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Time

interface LoadOffsetScheduleUseCase {
    operator fun invoke(offset: Int, refresh: Boolean): List<CoupleNative>
}