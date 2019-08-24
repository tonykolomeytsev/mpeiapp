package kekmech.ru.repository.gateways

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Schedule
import kekmech.ru.core.dto.ScheduleNative
import kekmech.ru.core.dto.WeekInfo
import kekmech.ru.core.gateways.ScheduleCacheGateway
import kekmech.ru.repository.room.AppDatabase
import javax.inject.Inject

class ScheduleCacheGatewayImpl @Inject constructor(val appdb: AppDatabase) : ScheduleCacheGateway {

    init {
        val schedule = Schedule(0, "С-12-16", 36, 1, ScheduleCacheGatewayImpl.allWeek, "Расписание на осенний семестр 2019/2020 учебного года")
        saveSchedule(schedule)
    }

    override fun getSchedule(): Schedule? =
        appdb.scheduleDao()
            .getAllByUserId(0)
            .firstOrNull()
            .let { Schedule(
                it?.id ?: 0,
                it?.group,
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
                schedule.group,
                schedule.calendarWeek,
                schedule.universityWeek,
                schedule.name
            ))
    }

    companion object {
        val monday by lazy { listOf(
            CoupleNative(0,0,
                "Защита интеллектуальной собственности и патентоведение",
                "Комерзан Е.В.",
                "C-213",
                "9:20",
                "10:50",
                CoupleNative.LECTURE,
                1,1, CoupleNative.ODD),
            CoupleNative(1, 0,
                "Вычислительная механика",
                "Адамов Б.И.",
                "C-213",
                "11:10",
                "12:45",
                CoupleNative.LECTURE,
                2,1, CoupleNative.BOTH),
            CoupleNative(2,0,
                "Вычислительная механика",
                "Адамов Б.И.",
                "C-213",
                "13:45",
                "15:20",
                CoupleNative.LAB,
                3,1, CoupleNative.BOTH),
            CoupleNative(3,0,
                "Гидропневмопривод мехатронных и робототехнических систем",
                "Зуев Ю.Ю.",
                "C-213",
                "15:35",
                "17:10",
                CoupleNative.PRACTICE,
                4,1, CoupleNative.BOTH)
        ) }

        val tuesday by lazy { listOf<CoupleNative>() }

        val wednesday by lazy { listOf(
            CoupleNative(1, 0,
                "Безопасность жизнедеятельности",
                "Боровкова А.М.",
                "Л-508",
                "11:10",
                "12:45",
                CoupleNative.LECTURE,
                2,3, CoupleNative.BOTH),
            CoupleNative(3,0,
                "Гидропневмопривод мехатронных и робототехнических систем",
                "Зуев Ю.Ю.",
                "C-213",
                "13:45",
                "15:20",
                CoupleNative.LECTURE,
                3,3, CoupleNative.BOTH),
            CoupleNative(2,0,
                "Научно исследовательская работа",
                "",
                "Кафедра РМД и ПМ",
                "15:35",
                "17:10",
                CoupleNative.LAB,
                4,3, CoupleNative.BOTH)
        ) }


        val thursday by lazy { listOf(
            CoupleNative(0,0,
                "Вычислительная механика",
                "Адамов Б.И.",
                "C-213",
                "9:20",
                "10:50",
                CoupleNative.COURSE,
                1,4, CoupleNative.ODD),
            CoupleNative(1, 0,
                "Основы мехатроники и робототехники",
                "Адамов Б.И.",
                "С-213",
                "11:10",
                "12:45",
                CoupleNative.LECTURE,
                2,4, CoupleNative.BOTH),
            CoupleNative(1, 0,
                "Безопасность жизнедеятельности",
                "Адамов Б.И.",
                "С-213",
                "11:10",
                "12:45",
                CoupleNative.LAB,
                2,4, CoupleNative.EVEN),
            CoupleNative(3,0,
                "Прикладные методы теории колебаний",
                "Кобрин А.Н.",
                "C-215",
                "13:45",
                "15:20",
                CoupleNative.LECTURE,
                3,4, CoupleNative.BOTH),
            CoupleNative(2,0,
                "Прикладные методы теории колебаний",
                "Панкратьева Г.В.",
                "C-215",
                "15:35",
                "17:10",
                CoupleNative.PRACTICE,
                4,4, CoupleNative.BOTH)
        ) }

        val friday by lazy { listOf(
            CoupleNative(0,0,
                "Защита интеллектуальной собественности и патентоведение",
                "Комерзан Е.В.",
                "C-213",
                "9:20",
                "10:50",
                CoupleNative.COURSE,
                1,5, CoupleNative.EVEN),
            CoupleNative(1, 0,
                "Основы мехатроники и робототехники",
                "Адамов Б.И.",
                "Кафедра РМД и ПМ",
                "11:10",
                "12:45",
                CoupleNative.LECTURE,
                2,5, CoupleNative.BOTH),
            CoupleNative(3,0,
                "Основы мехатроники и робототехники",
                "",
                "Кафедра РМД и ПМ",
                "13:45",
                "15:20",
                CoupleNative.PRACTICE,
                3,5, CoupleNative.ODD)
        ) }

        val allWeek by lazy { monday + tuesday + wednesday + thursday + friday }
    }
}