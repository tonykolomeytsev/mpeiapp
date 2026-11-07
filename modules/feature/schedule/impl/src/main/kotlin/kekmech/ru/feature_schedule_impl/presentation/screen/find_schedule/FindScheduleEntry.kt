package kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule

import androidx.compose.runtime.Composable
import com.kekmech.lib_fragment.HideKeyboardOnPause
import kekmech.ru.feature_schedule_impl.navigation.FindScheduleScreenKey
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleStoreFactory
import kekmech.ru.lib_elm_compose.ElmContent
import org.koin.compose.koinInject

@Composable
internal fun FindScheduleEntry(key: FindScheduleScreenKey) {
    HideKeyboardOnPause()
    val storeFactory = koinInject<FindScheduleStoreFactory>()
    ElmContent(
        key = "FindScheduleScreen",
        storeFactory = {
            storeFactory.create(
                resultKey = key.resultKey,
                continueTo = key.continueTo,
                selectGroupAfterSuccess = key.selectGroupAfterSuccess,
            )
        },
        content = ::FindScheduleScreen,
    )
}
