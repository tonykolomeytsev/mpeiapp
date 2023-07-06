package kekmech.ru.feature_schedule_api.data.repository

import kekmech.ru.feature_schedule_api.domain.model.Schedule
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule

interface ScheduleRepository {

    suspend fun getSchedule(type: ScheduleType, name: String, weekOffset: Int): Result<Schedule>

    fun setSelectedSchedule(selectedSchedule: SelectedSchedule)

    fun getSelectedSchedule(): SelectedSchedule
}
