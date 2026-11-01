package kekmech.ru.feature_app_settings_impl.presentation.screens.map_type

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kekmech.lib_fragment.ComposeBottomSheetDialogFragment
import com.kekmech.lib_fragment.LocalResultController
import kekmech.ru.ext_android.close
import kekmech.ru.ext_android.notNullStringArg
import kekmech.ru.ext_android.withArguments
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_app_settings_impl.presentation.screens.map_type.dto.MapTypeEntry
import kekmech.ru.feature_app_settings_impl.presentation.screens.select_option.SelectOptionScreen
import kekmech.ru.feature_app_settings_impl.presentation.screens.select_option.SelectableOption
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_strings.R.string as Strings

internal class SelectMapTypeFragment : ComposeBottomSheetDialogFragment() {

    private val selectedMapType by fastLazy { notNullStringArg(ARG_SELECTED_MAP_TYPE) }
    private val resultKey by fastLazy { notNullStringArg(ARG_RESULT_KEY) }
    private val analytics by screenAnalytics("SelectMapType")

    @Composable
    override fun Screen() {
        val resultController = LocalResultController.current
        MpeixTheme {
            SelectOptionScreen(
                title = stringResource(Strings.change_map_type_screen_title),
                options = MapTypeEntry.entries.map {
                    SelectableOption(
                        code = it.mapTypeCode,
                        titleRes = it.descriptionRes,
                    )
                },
                selectedCode = selectedMapType,
                onOptionClick = {
                    analytics.sendClick("SelectMapType_${it.code}")
                    if (it.code != selectedMapType) {
                        resultController.setResult(resultKey, it.code)
                    }
                    close()
                },
            )
        }
    }

    companion object {

        private const val ARG_SELECTED_MAP_TYPE = "Arg.Type"
        private const val ARG_RESULT_KEY = "Arg.ResultKey"

        fun newInstance(selectedMapType: String, resultKey: String) =
            SelectMapTypeFragment()
                .withArguments(
                    ARG_SELECTED_MAP_TYPE to selectedMapType,
                    ARG_RESULT_KEY to resultKey
                )
    }
}
