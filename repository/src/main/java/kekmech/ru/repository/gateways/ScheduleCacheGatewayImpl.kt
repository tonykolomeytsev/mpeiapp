package kekmech.ru.repository.gateways

import io.realm.Realm
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.gateways.ScheduleCacheGateway
import javax.inject.Inject

class ScheduleCacheGatewayImpl @Inject constructor(realm: Realm) : ScheduleCacheGateway {
    override fun get(dayNum: Int, odd: Boolean): List<CoupleNative> {
        return /*if (dayNum != 1) emptyList() else*/ listOf(
            CoupleNative(
                "Патентоведение",
                "Комерзан Е.В.",
                "C-213",
                "9:20",
                "10:50",
                CoupleNative.LECTURE,
                1,1),
            CoupleNative(
                "Вычислительная механика",
                "Адамов Б.И.",
                "C-213",
                "11:10",
                "12:45",
                CoupleNative.LECTURE,
                2,1),
            CoupleNative(type = CoupleNative.LUNCH),
            CoupleNative(
                "Вычислительная механика",
                "Адамов Б.И.",
                "C-213",
                "13:45",
                "15:20",
                CoupleNative.LAB,
                3,1),
            CoupleNative(
                "Гидропневмопривод",
                "Зуев Ю.Ю.",
                "C-213",
                "15:35",
                "17:10",
                CoupleNative.LECTURE,
                4,1)
            )
    }
}