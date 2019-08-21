package kekmech.ru.repository.gateways

import io.realm.Realm
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Schedule
import kekmech.ru.core.dto.WeekInfo
import kekmech.ru.core.gateways.ScheduleCacheGateway
import javax.inject.Inject

class ScheduleCacheGatewayImpl @Inject constructor(val realm: Realm) : ScheduleCacheGateway {
    override fun getSchedule(): Schedule? {
        var schedule: Schedule? = null
        realm.executeTransaction {
            schedule = it
                .where(Schedule::class.java)
                .findFirst()
        }
        return schedule
    }

    override fun getWeekInfo(): WeekInfo? {
        return getSchedule()?.weekInfo
    }

    override fun getCouples(dayNum: Int, odd: Boolean): List<CoupleNative>? {
        return getSchedule()?.coupleList
            ?.filter { it.day == dayNum && it.week == if (odd) CoupleNative.ODD else CoupleNative.EVEN }
            ?.sortedBy { it.num }
    }

    override fun saveSchedule(schedule: Schedule) {
        realm.executeTransaction {
            it.insertOrUpdate(schedule)
        }
    }

    companion object {
        val list by lazy { listOf(
            CoupleNative(
                "Защита интеллектуальной собственности и патентоведение",
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
                "Гидропневмопривод мехатронных и робототехнических систем",
                "Зуев Ю.Ю.",
                "C-213",
                "15:35",
                "17:10",
                CoupleNative.PRACTICE,
                4,1)
        ) }
    }
}