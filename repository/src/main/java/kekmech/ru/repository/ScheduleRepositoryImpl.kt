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

class ScheduleRepositoryImpl(
    private val appdb: AppDatabase,
    private val context: Context
) : ScheduleRepository {

    private val sharedPreferences = context.getSharedPreferences("mpeix", Context.MODE_PRIVATE)

    private val gson = GsonBuilder().create()

    override val schedule = MutableLiveData<Schedule>()

    override val groupNumber: LiveData<String> = Transformations.map(schedule) { it?.group }

    override val sessionSchedule = MutableLiveData<AcademicSession>()

    override suspend fun syncronize() = withContext(IO) {
        launch {
            try {
                val s = LoadScheduleFromRemoteInteractor(groupNumber.value!!)
                    .setAttempts(3)
                    .setDelay(1000)
                    .invoke()
                schedule.value = s!!
                // TODO кэшировать
            } catch (e: Exception) { Log.e("ScheduleRepository", "Unable to load semester schedule: $e") }
        }
        launch {
            try {
                val s = LoadSessionFromRemoteInteractor(groupNumber.value!!)
                    .setAttempts(1)
                    .setDelay(1000)
                    .invoke()
                sessionSchedule.value = s
                // TODO кэшировать
            } catch (e: Exception) { Log.e("ScheduleRepository", "Unable to load session schedule: $e") }
        }

        Unit
    }

    override suspend fun removeAllSchedules() {
        appdb.scheduleDao().getAll().forEach(appdb.scheduleDao()::delete)
        appdb.coupleDao().getAll().forEach(appdb.coupleDao()::delete)
        schedule.value = null
        sessionSchedule.value = null
    }

    override suspend fun addSchedule(groupNumber: String) {
        try {
            val s = LoadScheduleFromRemoteInteractor(groupNumber)
                .setAttempts(3)
                .setDelay(1000)
                .invoke()
            schedule.value = s!!
            // TODO кэшировать
        } catch (e: Exception) { Log.e("ScheduleRepository", "Unable to load semester schedule: $e") }
    }

    override suspend fun getAllSchedules(): List<ScheduleNative> {
        return appdb.scheduleDao().getAll()
    }

    private suspend fun getCurrentScheduleId(): Int {
        val users = appdb.userDao().getAll()
        if (users.isEmpty())
            appdb
                .userDao()
                .insert(User.defaultUser())
        val user = appdb.userDao().getAll().first()
        return user.lastScheduleId
    }

    private suspend fun setCurrentScheduleId(id: Int) {
        val user = appdb.userDao().getAll().first()
        appdb.userDao().update(user.apply { lastScheduleId = id })
    }

    private fun<T> async(action: suspend CoroutineScope.() -> T) = GlobalScope.async(Dispatchers.IO, block = action)

}