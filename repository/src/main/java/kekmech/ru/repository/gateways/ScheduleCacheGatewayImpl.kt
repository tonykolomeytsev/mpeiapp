package kekmech.ru.repository.gateways

import kekmech.ru.core.dto.*
import kekmech.ru.core.gateways.ScheduleCacheGateway
import kekmech.ru.repository.room.AppDatabase

class ScheduleCacheGatewayImpl constructor(val appdb: AppDatabase) : ScheduleCacheGateway {
    override var scheduleId: Int = 0

    override fun getSchedule(): Schedule? {
        val users = appdb.userDao().getAll()
        if (users.isEmpty())
            appdb
                .userDao()
                .insert(User.defaultUser())
        val user = appdb.userDao().getAll().first()
        scheduleId = user.lastScheduleId
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
        val user = appdb.userDao().getAll().first() // ТУТ БЫЛО НАПИСАНО last(). WHY???????
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

    override fun getAllSchedules(): List<ScheduleNative> {
        return appdb.scheduleDao().getAll()
    }

    override fun setCurrentScheduleId(id: Int) {
        val user = appdb.userDao().getAll().first()
        appdb.userDao().update(user.apply { lastScheduleId = id })
        scheduleId = id
    }
}