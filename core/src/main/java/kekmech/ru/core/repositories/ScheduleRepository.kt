package kekmech.ru.core.repositories

import kekmech.ru.core.dto.Couple
import kekmech.ru.core.dto.CoupleNative

interface ScheduleRepository {
    fun get(offset:Int, refresh: Boolean): List<CoupleNative>
}