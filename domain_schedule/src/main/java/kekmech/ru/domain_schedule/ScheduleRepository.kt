package kekmech.ru.domain_schedule

import kekmech.ru.domain_schedule.dto.GetScheduleBody

class ScheduleRepository(
    private val scheduleService: ScheduleService
) {

    fun loadSchedule(groupName: String, weekOffset: Int = 0) = scheduleService
        .getSchedule(GetScheduleBody(groupName, weekOffset))
}