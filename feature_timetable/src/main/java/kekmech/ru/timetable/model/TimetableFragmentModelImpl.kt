package kekmech.ru.timetable.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kekmech.ru.core.dto.*
import kekmech.ru.core.usecases.*
import kekmech.ru.timetable.R

class TimetableFragmentModelImpl constructor(
    private val getGroupNumberUseCase: GetGroupNumberUseCase,
    private val setIsShowedUpdateDialogUseCase: SetIsShowedUpdateDialogUseCase,
    private val getIsShowedUpdateDialogUseCase: GetIsShowedUpdateDialogUseCase,
    private val setForceUpdateDataUseCase: SetForceUpdateDataUseCase,
    private val setCreateNoteTransactionUseCase: SetCreateNoteTransactionUseCase,
    private val invokeUpdateScheduleUseCase: InvokeUpdateScheduleUseCase,
    private val getTimetableScheduleLiveDataUseCase: GetTimetableScheduleLiveDataUseCase,
    private val getNoteByTimestampUseCase: GetNoteByTimestampUseCase,
    private val context: Context
) : TimetableFragmentModel {

    override val today: Time
        get() = Time.today()

    /**
     * Group number like "C-12-16"
     */
    override val groupNumber: LiveData<String> get() = getGroupNumberUseCase()

    /**
     * Current week number
     */
    override val currentWeekNumber: Int
        get() = today.weekOfSemester

    override val formattedTodayStatus: String
        get() = "${today.formattedAsDayName(context, R.array.days_of_week)}, ${today.dayOfMonth} " +
                today.formattedAsMonthName(context, R.array.months)


    override var isNotShowedUpdateDialog: Boolean
        get() = getIsShowedUpdateDialogUseCase()
        set(value) { setIsShowedUpdateDialogUseCase(value) }

    override var weekOffset: LiveData<Int> = MutableLiveData<Int>().apply { value = 0 }

    override fun saveForceUpdateArgs(url: String, description: String) {
        setForceUpdateDataUseCase(url, description)
    }

    override fun transactCouple(coupleNative: CoupleNative, offset: Int) {
        setCreateNoteTransactionUseCase(NoteTransaction(coupleNative, Time.today().weekOfYear + offset))
    }

    override fun getNote(coupleNative: CoupleNative, offset: Int): NoteNative? {
        try {
            return getNoteByTimestampUseCase(coupleNative.scheduleId, NoteTimestamp.from(coupleNative, offset))
        } catch (e: Exception) { e.printStackTrace() }
        return null
    }

    override suspend fun updateScheduleFromRemote() {
        try {
            invokeUpdateScheduleUseCase()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override var selectedPage: Int = 0

    override val schedule: LiveData<Schedule>
        get() = getTimetableScheduleLiveDataUseCase()
}