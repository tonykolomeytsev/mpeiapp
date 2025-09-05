package kekmech.ru.domain_schedule.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kekmech.ru.domain_schedule.dto.SelectedSchedule
import kekmech.ru.domain_schedule_models.dto.Schedule
import kekmech.ru.domain_schedule_models.dto.ScheduleType

public interface ScheduleRepository {

    public fun getSchedule(type: ScheduleType, name: String, weekOffset: Int): Single<Schedule>

    public fun setSelectedSchedule(selectedSchedule: SelectedSchedule): Completable

    public fun getSelectedSchedule(): Single<SelectedSchedule>
}
