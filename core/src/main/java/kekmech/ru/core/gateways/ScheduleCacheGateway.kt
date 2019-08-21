package kekmech.ru.core.gateways

import kekmech.ru.core.dto.CoupleNative

/**
 * Yes, it's just a another called repository
 */
interface ScheduleCacheGateway {
    fun get(dayNum: Int, odd: Boolean): List<CoupleNative>
}