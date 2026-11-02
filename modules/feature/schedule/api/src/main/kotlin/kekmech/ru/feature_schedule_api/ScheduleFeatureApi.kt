package kekmech.ru.feature_schedule_api

import java.io.Serializable

public interface ScheduleFeatureApi {

    /**
     * Открывает экран поиска группы поверх текущего экрана
     */
    public fun launchSearchGroup(
        continueTo: ContinueTo = ContinueTo.BACK,
        selectGroupAfterSuccess: Boolean = true,
        resultKey: String,
    )

    public enum class ContinueTo : Serializable { DASHBOARD, BACK, BACK_WITH_RESULT }
}
