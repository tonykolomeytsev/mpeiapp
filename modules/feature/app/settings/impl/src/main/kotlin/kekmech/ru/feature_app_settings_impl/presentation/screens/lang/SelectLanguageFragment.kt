package kekmech.ru.feature_app_settings_impl.presentation.screens.lang

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kekmech.lib_fragment.ComposeBottomSheetDialogFragment
import com.kekmech.lib_fragment.LocalResultController
import kekmech.ru.ext_android.close
import kekmech.ru.ext_android.notNullStringArg
import kekmech.ru.ext_android.withArguments
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_app_settings_impl.presentation.screens.lang.dto.LanguageEntry
import kekmech.ru.feature_app_settings_impl.presentation.screens.select_option.SelectOptionScreen
import kekmech.ru.feature_app_settings_impl.presentation.screens.select_option.SelectableOption
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_strings.R.string as Strings

internal class SelectLanguageFragment : ComposeBottomSheetDialogFragment() {

    private val selectedLanguage by fastLazy { notNullStringArg(ARG_SELECTED_LANG) }
    private val resultKey by fastLazy { notNullStringArg(ARG_RESULT_KEY) }
    private val analytics by screenAnalytics("SelectLanguage")

    @Composable
    override fun Screen() {
        val resultController = LocalResultController.current
        MpeixTheme {
            SelectOptionScreen(
                title = stringResource(Strings.change_language_screen_title),
                options = LanguageEntry.entries.map {
                    SelectableOption(
                        code = it.languageCode,
                        titleRes = it.descriptionRes,
                        iconRes = it.iconRes,
                    )
                },
                selectedCode = selectedLanguage,
                onOptionClick = {
                    analytics.sendClick("SelectLanguage_${it.code}")
                    if (it.code != selectedLanguage) {
                        resultController.setResult(resultKey, it.code)
                    }
                    close()
                },
                footer = stringResource(Strings.change_language_disclaimer),
            )
        }
    }

    companion object {

        private const val ARG_SELECTED_LANG = "Arg.Lang"
        private const val ARG_RESULT_KEY = "Arg.ResultKey"

        fun newInstance(selectedLanguage: String, resultKey: String) =
            SelectLanguageFragment()
                .withArguments(
                    ARG_SELECTED_LANG to selectedLanguage,
                    ARG_RESULT_KEY to resultKey
                )
    }
}
