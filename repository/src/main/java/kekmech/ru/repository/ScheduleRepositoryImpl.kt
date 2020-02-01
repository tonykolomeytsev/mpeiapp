package kekmech.ru.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.gson.GsonBuilder
import kekmech.ru.core.dto.*
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.repository.gateways.LoadScheduleFromRemoteInteractor
import kekmech.ru.repository.gateways.LoadSessionFromRemoteInteractor
import kekmech.ru.repository.room.AppDatabase
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class ScheduleRepositoryImpl(
    private val appdb: AppDatabase,
    private val context: Context
) : ScheduleRepository {

    private val sharedPreferences = context.getSharedPreferences("mpeix", Context.MODE_PRIVATE)

    private val gson = GsonBuilder().create()

    override val schedule = MutableLiveData<Schedule>()

    override val groupNumber: LiveData<String> = Transformations.map(schedule) { it?.group }

    override val sessionSchedule = MutableLiveData<AcademicSession>()

    /**
     * Запуск синхронизации данных с сайтом.
     * Загружается и семестровое расписание, и сессионное
     */
    override suspend fun syncronize() = withContext(IO) {
        loadScheduleFromCache()?.let { withContext(Main) { schedule.value = it } } // грузим расписание из кэша
        launch {
            try {
                val s = LoadScheduleFromRemoteInteractor(schedule.value!!.group)
                    .setAttempts(3)
                    .setDelay(1000)
                    .invoke()
                saveScheduleToCache(s!!) // кэшируем загруженное расписание
                withContext(Main) { schedule.value = s }
            } catch (e: Exception) { Log.e("ScheduleRepository", "Unable to load semester schedule: $e") }
        }
        launch {
            try {
                val s = LoadSessionFromRemoteInteractor(schedule.value!!.group)
                    .setAttempts(1)
                    .setDelay(1000)
                    .invoke()
                withContext(Main) { sessionSchedule.value = s }
                // TODO кэшировать
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ScheduleRepository", "Unable to load session schedule: $e") }
        }

        Unit
    }

    /**
     * Удаление всех расписаний
     */
    override suspend fun removeAllSchedules() {
        appdb.scheduleDao().getAll().forEach(appdb.scheduleDao()::delete)
        appdb.coupleDao().getAll().forEach(appdb.coupleDao()::delete)
        withContext(Main) {
            schedule.value = null
            sessionSchedule.value = null
        }
    }

    /**
     * Загрузка нового расписания по номеру группы,
     * если расписание для этой группы уже существует, то будет сначала загружено из кэша,
     * после чего асинхронно запустится синхронизация с сайтом (если sync == true)
     */
    override suspend fun addSchedule(groupNumber: String, sync: Boolean): Boolean {
        if (sync) {
            try {
                val s = LoadScheduleFromRemoteInteractor(groupNumber)
                    .setAttempts(3)
                    .setDelay(1000)
                    .invoke()
                saveScheduleToCache(s!!) // кэшируем загруженное расписание
                withContext(Main) { schedule.value = s }
                return true
            } catch (e: Exception) { Log.e("ScheduleRepository", "Unable to load semester schedule: $e") }
        } else {
            appdb.scheduleDao().getByGroupNum(groupNumber)?.let { schedule ->
                setCurrentScheduleId(schedule.id)
                loadScheduleFromCache()?.let { withContext(Main) { this@ScheduleRepositoryImpl.schedule.value = it } }
            }
            return true
        }
        return false
    }

    /**
     * Получить все расписания
     */
    override suspend fun getAllSchedules() = appdb.scheduleDao().getAll()

    /**
     * Есть ли вообще расписания
     */
    override suspend fun isSchedulesEmpty(): Boolean {
        return appdb.scheduleDao().getAnySchedule() == null
    }

    private suspend fun getCurrentScheduleId(): Int {
        val users = appdb.userDao().getAll()
        if (users.isEmpty()) appdb.userDao().insert(User.defaultUser())
        val user = appdb.userDao().getAll().first()
        return user.lastScheduleId
    }

    private suspend fun setCurrentScheduleId(id: Int) {
        val user = appdb.userDao().getAll().first()
        appdb.userDao().update(user.apply { lastScheduleId = id })
    }

    /**
     * Загрузка ПОСЛЕДНЕГО просматриваемого расписания из кэша
     */
    private suspend fun loadScheduleFromCache(): Schedule? {
        return appdb.scheduleDao()
            .getById(getCurrentScheduleId())
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

    /**
     * Кэширование расписания
     * Если расписание с таким номером группы уже существует, его пары будут перезаписаны,
     * иначе, будет создано новое расписание
     */
    private suspend fun saveScheduleToCache(schedule: Schedule) {
        val similarSchedule = appdb.scheduleDao().getByGroupNum(schedule.group)
        if (similarSchedule == null) {
            createNewSchedule(schedule)
            Log.d("ScheduleRepository", "Schedule created")
        } else {
            // тут надо очистить все пары и добавить их заново, а у schedule поменять calendarWeek на свежий
            val scheduleId = similarSchedule.id
            appdb.coupleDao().deleteByScheduleId(scheduleId) // очищаем все пары
            // устанавливаем свежую дату
            similarSchedule.calendarWeek = schedule.calendarWeek
            similarSchedule.universityWeek = schedule.universityWeek
            // достаём новые пары
            val newCouples = schedule.coupleList.map { it.apply { this.scheduleId = scheduleId } }
            newCouples.forEach(appdb.coupleDao()::insert) // пишем новые пары в базу

            Log.d("ScheduleRepository", "Schedule updated")
        }
    }

    private suspend fun createNewSchedule(schedule: Schedule) {
        val native = ScheduleNative(
            0,
            schedule.group,
            schedule.calendarWeek,
            schedule.universityWeek,
            schedule.name
        )

        appdb.scheduleDao().insert(native)
        val id = appdb.scheduleDao().getByGroupNum(schedule.group)!!.id
        schedule.coupleList.forEach { appdb.coupleDao().insert(it.apply { this.scheduleId = id }) }
        setCurrentScheduleId(id)
    }

    private fun<T> async(action: suspend CoroutineScope.() -> T) = GlobalScope.async(Dispatchers.IO, block = action)

}