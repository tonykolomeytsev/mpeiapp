package kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule

import androidx.compose.runtime.Composable
import com.kekmech.lib_fragment.ComposeFragment
import kekmech.ru.ext_android.booleanArg
import kekmech.ru.ext_android.hideKeyboard
import kekmech.ru.ext_android.notNullSerializableArg
import kekmech.ru.ext_android.notNullStringArg
import kekmech.ru.ext_android.withArguments
import kekmech.ru.feature_schedule_api.ScheduleFeatureApi
import kekmech.ru.feature_schedule_api.ScheduleFeatureApi.ContinueTo.BACK
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleStoreFactory
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.compose.koinInject

internal class FindScheduleFragment : ComposeFragment() {

    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }

    @Composable
    override fun Screen() {
        MpeixTheme {
            val storeFactory = koinInject<FindScheduleStoreFactory>()
            ElmContent(
                key = "FindScheduleScreen",
                storeFactory = {
                    storeFactory.create(
                        resultKey = notNullStringArg(ARG_RESULT_KEY),
                        continueTo = notNullSerializableArg(ARG_CONTINUE_TO),
                        selectGroupAfterSuccess = booleanArg(ARG_SELECT_AFTER),
                    )
                },
                content = ::FindScheduleScreen,
            )
        }
    }

    companion object {

        private const val ARG_CONTINUE_TO = "Arg.ContinueTo"
        private const val ARG_SELECT_AFTER = "Arg.SelectAfter"
        private const val ARG_RESULT_KEY = "Arg.ResultKey"

        fun newInstance(
            continueTo: ScheduleFeatureApi.ContinueTo = BACK,
            selectGroupAfterSuccess: Boolean = true,
            resultKey: String,
        ) = FindScheduleFragment().withArguments(
            ARG_CONTINUE_TO to continueTo,
            ARG_SELECT_AFTER to selectGroupAfterSuccess,
            ARG_RESULT_KEY to resultKey,
        )
    }
}
