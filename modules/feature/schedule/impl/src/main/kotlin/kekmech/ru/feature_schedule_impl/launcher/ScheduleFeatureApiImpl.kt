package kekmech.ru.feature_schedule_impl.launcher

import kekmech.ru.feature_schedule_api.ScheduleFeatureApi
import kekmech.ru.feature_schedule_impl.navigation.FindScheduleScreenKey
import kekmech.ru.lib_navigation.AddScreenForward
import kekmech.ru.lib_navigation.Router

internal class ScheduleFeatureApiImpl(
    private val router: Router,
) : ScheduleFeatureApi {

    override fun launchSearchGroup(
        continueTo: ScheduleFeatureApi.ContinueTo,
        selectGroupAfterSuccess: Boolean,
        resultKey: String,
    ) {
        router.executeCommand(
            AddScreenForward(
                FindScheduleScreenKey(
                    continueTo = continueTo,
                    selectGroupAfterSuccess = selectGroupAfterSuccess,
                    resultKey = resultKey,
                )
            )
        )
    }
}
