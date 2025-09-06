package kekmech.ru.feature_schedule_api.data.repository

import kekmech.ru.feature_schedule_api.domain.model.Schedule
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule

public interface ScheduleRepository {

    public suspend fun getSchedule(type: ScheduleType, name: String, weekOffset: Int): Result<Schedule>

    public fun setSelectedSchedule(selectedSchedule: SelectedSchedule)

    public fun getSelectedSchedule(): SelectedSchedule
}
