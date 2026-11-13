package kekmech.ru.feature_app_settings_impl.presentation.screens.lang

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kekmech.lib_fragment.LocalResultController
import com.kekmech.lib_fragment.LocalRouter
import kekmech.ru.feature_app_settings_impl.navigation.SelectLanguageScreenKey
import kekmech.ru.feature_app_settings_impl.presentation.screens.lang.dto.LanguageEntry
import kekmech.ru.feature_app_settings_impl.presentation.screens.select_option.SelectOptionScreen
import kekmech.ru.feature_app_settings_impl.presentation.screens.select_option.SelectableOption
import kekmech.ru.lib_analytics_android.ext.rememberScreenAnalytics
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.res_strings.R.string as Strings

private val LanguageOptions = LanguageEntry.entries.map {
    SelectableOption(
        code = it.languageCode,
        titleRes = it.descriptionRes,
        iconRes = it.iconRes,
    )
}

@Composable
internal fun SelectLanguageEntry(key: SelectLanguageScreenKey) {
    val analytics = rememberScreenAnalytics("SelectLanguage")
    val router = LocalRouter.current
    val resultController = LocalResultController.current
    SelectOptionScreen(
        title = stringResource(Strings.change_language_screen_title),
        options = LanguageOptions,
        selectedCode = key.selectedLanguage,
        onOptionClick = {
            analytics.sendClick("SelectLanguage_${it.code}")
            if (it.code != key.selectedLanguage) {
                resultController.setResult(key.resultKey, it.code)
            }
            router.executeCommand(PopBackStack())
        },
        footer = stringResource(Strings.change_language_disclaimer),
    )
}
