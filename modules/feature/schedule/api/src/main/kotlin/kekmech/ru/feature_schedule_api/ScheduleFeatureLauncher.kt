package kekmech.ru.feature_schedule_api

import androidx.fragment.app.Fragment
import java.io.Serializable

public interface ScheduleFeatureLauncher {

    public fun getScreen(): Fragment

    public fun launchSearchGroup(
        continueTo: ContinueTo = ContinueTo.BACK,
        selectGroupAfterSuccess: Boolean = true,
        resultKey: String,
    )

    public enum class ContinueTo : Serializable { DASHBOARD, BACK, BACK_WITH_RESULT }
}
