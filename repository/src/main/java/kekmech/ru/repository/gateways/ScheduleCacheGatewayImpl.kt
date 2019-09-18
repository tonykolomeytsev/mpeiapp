package kekmech.ru.repository.gateways

import android.util.Log
import kekmech.ru.core.dto.*
import kekmech.ru.core.gateways.ScheduleCacheGateway
import kekmech.ru.repository.room.AppDatabase
import javax.inject.Inject

class ScheduleCacheGatewayImpl @Inject constructor(val appdb: AppDatabase) : ScheduleCacheGateway {
    override fun getSchedule(): Schedule? {
        val users = appdb.userDao().getAll()
        if (users.isEmpty())
            appdb
                .userDao()
                .insert(User.defaultUser())
        val user = appdb.userDao().getAll().first()
        return appdb.scheduleDao()
            .getById(user.lastScheduleId)
            .let {
                if (it != null) Schedule(
                    it.id,
                    it.group,
                    it.calendarWeek,
                    it.universityWeek,
                    appdb.coupleDao().getAllByScheduleId(it.id),
                    it.name
                ) else null
            }
    }


    override fun getWeekInfo(): WeekInfo? =
        getSchedule().let { WeekInfo(it?.calendarWeek, it?.universityWeek) }

    override fun getCouples(dayNum: Int, odd: Boolean): List<CoupleNative>? {
        return getSchedule()?.coupleList
            ?.filter { it.day == dayNum }
            ?.filter { it.week == CoupleNative.BOTH || it.week == if (odd) CoupleNative.ODD else CoupleNative.EVEN }
            ?.sortedBy { it.num }
    }

    override fun newSchedule(schedule: Schedule) {
        val native = ScheduleNative(
            0,
            schedule.group,
            schedule.calendarWeek,
            schedule.universityWeek,
            schedule.name
        )

        appdb.scheduleDao().insert(native)
        val id = appdb.scheduleDao().getAll().last().id
        schedule.coupleList
            .forEach { appdb.coupleDao().insert(it.apply { this.scheduleId = id }) }
        val user = appdb.userDao().getAll().last()
        appdb.userDao().update(user.apply { lastScheduleId = id })
    }

    override fun getGroupNum(): String {
        val users = appdb.userDao().getAll()
        if (users.isEmpty())
            appdb
                .userDao()
                .insert(User.defaultUser())
        val user = appdb.userDao().getAll().first()
        return appdb.scheduleDao()
            .getById(user.lastScheduleId)
            ?.group ?: ""
    }

    companion object {
        val monday by lazy { listOf(
            CoupleNative(1,
                "Защита интеллектуальной собственности и патентоведение",
                "Комерзан Е.В.",
                "C-213",
                "9:20",
                "10:50",
                CoupleNative.LECTURE,
                1,1, CoupleNative.ODD),
            CoupleNative(1,
                "Вычислительная механика",
                "Адамов Б.И.",
                "C-213",
                "11:10",
                "12:45",
                CoupleNative.LECTURE,
                2,1, CoupleNative.BOTH),
            CoupleNative(1,
                "Вычислительная механика",
                "Адамов Б.И.",
                "C-213",
                "13:45",
                "15:20",
                CoupleNative.LAB,
                3,1, CoupleNative.BOTH),
            CoupleNative(1,
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
            CoupleNative(1,
                "Безопасность жизнедеятельности",
                "Боровкова А.М.",
                "Л-508",
                "11:10",
                "12:45",
                CoupleNative.LECTURE,
                2,3, CoupleNative.BOTH),
            CoupleNative(1,
                "Гидропневмопривод мехатронных и робототехнических систем",
                "Зуев Ю.Ю.",
                "C-213",
                "13:45",
                "15:20",
                CoupleNative.LECTURE,
                3,3, CoupleNative.BOTH),
            CoupleNative(1,
                "Научно исследовательская работа",
                "",
                "Кафедра РМД и ПМ",
                "15:35",
                "17:10",
                CoupleNative.LAB,
                4,3, CoupleNative.BOTH)
        ) }


        val thursday by lazy { listOf(
            CoupleNative(1,
                "Вычислительная механика",
                "Адамов Б.И.",
                "C-213",
                "9:20",
                "10:50",
                CoupleNative.COURSE,
                1,4, CoupleNative.ODD),
            CoupleNative(1,
                "Основы мехатроники и робототехники",
                "Адамов Б.И.",
                "С-213",
                "11:10",
                "12:45",
                CoupleNative.LECTURE,
                2,4, CoupleNative.BOTH),
            CoupleNative(1,
                "Безопасность жизнедеятельности",
                "Адамов Б.И.",
                "С-213",
                "11:10",
                "12:45",
                CoupleNative.LAB,
                2,4, CoupleNative.EVEN),
            CoupleNative(1,
                "Прикладные методы теории колебаний",
                "Кобрин А.Н.",
                "C-215",
                "13:45",
                "15:20",
                CoupleNative.LECTURE,
                3,4, CoupleNative.BOTH),
            CoupleNative(1,
                "Прикладные методы теории колебаний",
                "Панкратьева Г.В.",
                "C-215",
                "15:35",
                "17:10",
                CoupleNative.PRACTICE,
                4,4, CoupleNative.BOTH)
        ) }

        val friday by lazy { listOf(
            CoupleNative(1,
                "Защита интеллектуальной собественности и патентоведение",
                "Комерзан Е.В.",
                "C-213",
                "9:20",
                "10:50",
                CoupleNative.COURSE,
                1,5, CoupleNative.EVEN),
            CoupleNative(1,
                "Основы мехатроники и робототехники",
                "Адамов Б.И.",
                "Кафедра РМД и ПМ",
                "11:10",
                "12:45",
                CoupleNative.LECTURE,
                2,5, CoupleNative.BOTH),
            CoupleNative(1,
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