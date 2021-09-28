package kekmech.ru.domain_schedule

import android.os.Parcelable
import androidx.fragment.app.Fragment
import kotlinx.parcelize.Parcelize

interface ScheduleFeatureLauncher {

    fun getScreen(): Fragment

    fun launchSearchGroup(
        continueTo: ContinueTo = ContinueTo.BACK,
        selectGroupAfterSuccess: Boolean = true,
        resultKey: String,
    )

    @Parcelize
    enum class ContinueTo : Parcelable { DASHBOARD, BACK, BACK_WITH_RESULT }
}