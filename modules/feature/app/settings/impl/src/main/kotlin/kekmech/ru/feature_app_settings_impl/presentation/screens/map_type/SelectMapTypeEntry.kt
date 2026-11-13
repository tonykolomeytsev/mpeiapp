package kekmech.ru.feature_app_settings_impl.presentation.screens.map_type

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kekmech.lib_fragment.LocalResultController
import com.kekmech.lib_fragment.LocalRouter
import kekmech.ru.feature_app_settings_impl.navigation.SelectMapTypeScreenKey
import kekmech.ru.feature_app_settings_impl.presentation.screens.map_type.dto.MapTypeEntry
import kekmech.ru.feature_app_settings_impl.presentation.screens.select_option.SelectOptionScreen
import kekmech.ru.feature_app_settings_impl.presentation.screens.select_option.SelectableOption
import kekmech.ru.lib_analytics_android.ext.rememberScreenAnalytics
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.res_strings.R.string as Strings

private val MapTypeOptions = MapTypeEntry.entries.map {
    SelectableOption(
        code = it.mapTypeCode,
        titleRes = it.descriptionRes,
    )
}

@Composable
internal fun SelectMapTypeEntry(key: SelectMapTypeScreenKey) {
    val analytics = rememberScreenAnalytics("SelectMapType")
    val router = LocalRouter.current
    val resultController = LocalResultController.current
    SelectOptionScreen(
        title = stringResource(Strings.change_map_type_screen_title),
        options = MapTypeOptions,
        selectedCode = key.selectedMapType,
        onOptionClick = {
            analytics.sendClick("SelectMapType_${it.code}")
            if (it.code != key.selectedMapType) {
                resultController.setResult(key.resultKey, it.code)
            }
            router.executeCommand(PopBackStack())
        },
    )
}
