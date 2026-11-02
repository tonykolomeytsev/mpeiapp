package kekmech.ru.feature_schedule_impl.navigation

import kekmech.ru.feature_schedule_api.ScheduleFeatureApi
import kekmech.ru.lib_navigation.ScreenKey
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class FindScheduleScreenKey(
    val continueTo: ScheduleFeatureApi.ContinueTo,
    val selectGroupAfterSuccess: Boolean,
    val resultKey: String,
) : ScreenKey
