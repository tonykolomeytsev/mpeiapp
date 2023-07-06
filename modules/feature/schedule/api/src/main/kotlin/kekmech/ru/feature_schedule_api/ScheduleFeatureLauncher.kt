package kekmech.ru.feature_schedule_api

import androidx.fragment.app.Fragment
import java.io.Serializable

interface ScheduleFeatureLauncher {

    fun getScreen(): Fragment

    fun launchSearchGroup(
        continueTo: ContinueTo = ContinueTo.BACK,
        selectGroupAfterSuccess: Boolean = true,
        resultKey: String,
    )

    enum class ContinueTo : Serializable { DASHBOARD, BACK, BACK_WITH_RESULT }
}
