package kekmech.ru.timetable.model

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.NoteNative
import kekmech.ru.core.dto.Schedule
import kekmech.ru.core.dto.Time

interface TimetableFragmentModel {
    val today: Time
    /**
     * Group number like "C-12-16"
     */
    val groupNumber: LiveData<String>

    /**
     * Current week number
     */
    val currentWeekNumber: Int

    val formattedTodayStatus: String

    var weekOffset: LiveData<Int>

    var isNotShowedUpdateDialog: Boolean

    fun saveForceUpdateArgs(url: String, description: String)

    fun transactCouple(coupleNative: CoupleNative, offset: Int)

    suspend fun updateScheduleFromRemote()

    fun getNote(coupleNative: CoupleNative, offset: Int): NoteNative?

    var selectedPage: Int

    val schedule: LiveData<Schedule>
}