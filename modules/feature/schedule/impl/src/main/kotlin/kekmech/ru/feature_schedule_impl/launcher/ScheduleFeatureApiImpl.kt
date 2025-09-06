package kekmech.ru.feature_schedule_impl.launcher

import androidx.fragment.app.Fragment
import kekmech.ru.feature_schedule_api.ScheduleFeatureApi
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.FindScheduleFragment
import kekmech.ru.feature_schedule_impl.presentation.screen.main.ScheduleFragment

internal class ScheduleFeatureApiImpl : ScheduleFeatureApi {

    override fun getTabScreen(): Fragment = ScheduleFragment()

    override fun getSearchGroupScreen(
        continueTo: ScheduleFeatureApi.ContinueTo,
        selectGroupAfterSuccess: Boolean,
        resultKey: String
    ): Fragment = FindScheduleFragment.newInstance(
        continueTo = continueTo,
        selectGroupAfterSuccess = selectGroupAfterSuccess,
        resultKey = resultKey
    )
}
