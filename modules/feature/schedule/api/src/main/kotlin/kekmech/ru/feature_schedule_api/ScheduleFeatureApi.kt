package kekmech.ru.feature_schedule_api

import androidx.fragment.app.Fragment
import java.io.Serializable

public interface ScheduleFeatureApi {

    public fun getTabScreen(): Fragment

    public fun getSearchGroupScreen(
        continueTo: ContinueTo = ContinueTo.BACK,
        selectGroupAfterSuccess: Boolean = true,
        resultKey: String,
    ): Fragment

    public enum class ContinueTo : Serializable { DASHBOARD, BACK, BACK_WITH_RESULT }
}
