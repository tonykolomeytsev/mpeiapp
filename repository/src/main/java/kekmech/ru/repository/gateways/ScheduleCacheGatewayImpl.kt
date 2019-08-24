package kekmech.ru.repository.gateways

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Schedule
import kekmech.ru.core.dto.ScheduleNative
import kekmech.ru.core.dto.WeekInfo
import kekmech.ru.core.gateways.ScheduleCacheGateway
import kekmech.ru.repository.room.AppDatabase
import javax.inject.Inject

class ScheduleCacheGatewayImpl @Inject constructor(val appdb: AppDatabase) : ScheduleCacheGateway {
    override fun getSchedule(): Schedule? =
        appdb.scheduleDao()
            .getAllByUserId(0)
            .firstOrNull()
            .let { Schedule(
                it?.id ?: 0,
                it?.calendarWeek,
                it?.universityWeek,
                appdb.coupleDao().getAllByScheduleId(it?.id ?: 0),
                it?.name
            ) }


    override fun getWeekInfo(): WeekInfo? =
        getSchedule().let { WeekInfo(it?.calendarWeek, it?.universityWeek) }

    override fun getCouples(dayNum: Int, odd: Boolean): List<CoupleNative>? {
        return getSchedule()?.coupleList
            ?.filter { it.day == dayNum }
            ?.filter { it.week == CoupleNative.BOTH || it.week == if (odd) CoupleNative.ODD else CoupleNative.EVEN }
            ?.sortedBy { it.num }
    }

    override fun saveSchedule(schedule: Schedule) {
        schedule.coupleList
            ?.forEach { appdb.coupleDao().update(it) }
        appdb.scheduleDao()
            .update(ScheduleNative(
                schedule.id,
                0,
                schedule.calendarWeek,
                schedule.universityWeek,
                schedule.name
            ))
    }

    companion object {
        val list by lazy { listOf(
            CoupleNative(0,0,
                "Защита интеллектуальной собственности и патентоведение",
                "Комерзан Е.В.",
                "C-213",
                "9:20",
                "10:50",
                CoupleNative.LECTURE,
                1,1),
            CoupleNative(1, 0,
                "Вычислительная механика",
                "Адамов Б.И.",
                "C-213",
                "11:10",
                "12:45",
                CoupleNative.LECTURE,
                2,1),
            CoupleNative(type = CoupleNative.LUNCH),
            CoupleNative(2,0,
                "Вычислительная механика",
                "Адамов Б.И.",
                "C-213",
                "13:45",
                "15:20",
                CoupleNative.LAB,
                3,1),
            CoupleNative(3,0,
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