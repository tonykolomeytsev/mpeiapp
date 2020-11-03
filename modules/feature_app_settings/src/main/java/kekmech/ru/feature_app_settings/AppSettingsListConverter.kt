package kekmech.ru.feature_app_settings

import kekmech.ru.coreui.items.*
import kekmech.ru.feature_app_settings.AppSettingsFragment.Companion.ITEM_GITHUB
import kekmech.ru.feature_app_settings.AppSettingsFragment.Companion.ITEM_SUPPORT
import kekmech.ru.feature_app_settings.presentation.AppSettingsState

internal class AppSettingsListConverter {

    fun map(state: AppSettingsState): List<Any> {
        val appSettings = state.appSettings ?: return emptyList()
        return mutableListOf<Any>().apply {
            add(ToggleItem(
                itemId = AppSettingsFragment.TOGGLE_DARK_THEME,
                titleRes = R.string.app_settings_dark_theme,
                isChecked = appSettings.isDarkThemeEnabled
            ))
            add(SectionTextItem(resId = R.string.app_settings_dark_theme_description))
            add(SpaceItem.VERTICAL_24)

            add(SectionHeaderItem(
                titleRes = R.string.app_settings_header_schedule
            ))
            add(ToggleItem(
                itemId = AppSettingsFragment.TOGGLE_CHANGE_DAY_AFTER_CHANGE_WEEK,
                titleRes = R.string.app_settings_change_day_after_change_week,
                isChecked = appSettings.changeDayAfterChangeWeek
            ))
            add(SectionTextItem(resId = R.string.app_settings_change_day_after_change_week_description))
            add(SpaceItem.VERTICAL_24)

            add(SectionHeaderItem(
                titleRes = R.string.app_settings_header_map
            ))
            add(ToggleItem(
                itemId = AppSettingsFragment.TOGGLE_AUTO_HIDE_BOTTOM_SHEET,
                titleRes = R.string.app_settings_auto_hide_bottom_sheet,
                isChecked = appSettings.autoHideBottomSheet
            ))
            add(SectionTextItem(resId = R.string.app_settings_auto_hide_bottom_sheet_description))

            add(SpaceItem.VERTICAL_24)
            add(SectionHeaderItem(title = "Поддержка"))
            add(SpaceItem.VERTICAL_12)
            add(BottomLabeledTextItem(
                mainText = "Задать вопрос в группу",
                label = "vk.com/kekmech",
                itemId = ITEM_SUPPORT
            ))
            add(BottomLabeledTextItem(
                mainText = "Завести Issue или Pull Request",
                label = "github.com/tonykolomeytsev/mpeiapp",
                itemId = ITEM_GITHUB
            ))

            if (BuildConfig.DEBUG) {
                add(SpaceItem.VERTICAL_24)
                add(SectionHeaderItem(title = "Дебаг"))
                add(SpaceItem.VERTICAL_12)
                add(BottomLabeledTextItem(
                    mainText = "Очистить поле selected_group",
                    label = "После очистки будет открыт онбординг",
                    itemId = AppSettingsFragment.ITEM_DEBUG_CLEAR_SELECTED_GROUP
                ))
            }
        }
    }
}