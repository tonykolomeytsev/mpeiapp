package kekmech.ru.core.repositories

import kekmech.ru.core.dto.Couple
import kekmech.ru.core.dto.CoupleNative

interface ScheduleRepository {
    fun get(refresh: Boolean): List<CoupleNative>
}