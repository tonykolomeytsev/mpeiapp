package kekmech.ru.domain_schedule.data

import kekmech.ru.domain_schedule.dto.SelectedSchedule
import kekmech.ru.domain_schedule_models.dto.Schedule
import kekmech.ru.domain_schedule_models.dto.ScheduleType

interface ScheduleRepository {

    suspend fun getSchedule(type: ScheduleType, name: String, weekOffset: Int): Result<Schedule>

    fun setSelectedSchedule(selectedSchedule: SelectedSchedule)

    fun getSelectedSchedule(): SelectedSchedule
}
