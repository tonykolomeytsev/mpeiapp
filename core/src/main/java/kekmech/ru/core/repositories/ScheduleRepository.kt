package kekmech.ru.core.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kekmech.ru.core.dto.AcademicSession
import kekmech.ru.core.dto.Schedule
import kekmech.ru.core.dto.ScheduleNative

interface ScheduleRepository {

    /**
     * Расписание семестра
     */
    val schedule: MutableLiveData<Schedule>
    /**
     * Номер академической группы, подтягивается из [schedule]
     */
    val groupNumber: LiveData<String>

    /**
     * Расписание сессии
     */
    val sessionSchedule: MutableLiveData<AcademicSession>

    /**
     * Запуск синхронизации данных с сайтом.
     * Загружается и семестровое расписание, и сессионное
     */
    suspend fun syncronize()

    /**
     * Загрузка нового расписания по номеру группы
     */
    suspend fun addSchedule(groupNumber: String)

    /**
     * Удаление всех расписаний
     */
    suspend fun removeAllSchedules()

    /**
     * Получить все расписания
     */
    suspend fun getAllSchedules(): List<ScheduleNative>
}