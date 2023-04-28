package kekmech.ru.domain_schedule.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kekmech.ru.domain_schedule.dto.SelectedSchedule
import kekmech.ru.domain_schedule_models.dto.Schedule
import kekmech.ru.domain_schedule_models.dto.ScheduleType

interface ScheduleRepository {

    fun getSchedule(type: ScheduleType, name: String, weekOffset: Int): Single<Schedule>

    fun setSelectedSchedule(selectedSchedule: SelectedSchedule): Completable

    fun getSelectedSchedule(): Single<SelectedSchedule>
}
