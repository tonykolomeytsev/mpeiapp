package kekmech.ru.feature_app_settings

import kekmech.ru.coreui.items.*
import kekmech.ru.feature_app_settings.presentation.AppSettingsState

class AppSettingsListConverter {

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
                titleRes = R.string.app_settings_header_schedule,
                itemId = AppSettingsFragment.SECTION_HEADER_SCHEDULE
            ))
            add(ToggleItem(
                itemId = AppSettingsFragment.TOGGLE_CHANGE_DAY_AFTER_CHANGE_WEEK,
                titleRes = R.string.app_settings_change_day_after_change_week,
                isChecked = appSettings.changeDayAfterChangeWeek
            ))
            add(SectionTextItem(resId = R.string.app_settings_change_day_after_change_week_description))

            add(SpaceItem.VERTICAL_24)
            add(SectionHeaderItem(
                title = "Поддержка",
                itemId = AppSettingsFragment.SECTION_HEADER_SUPPORT
            ))
            add(SpaceItem.VERTICAL_12)
            add(BottomLabeledTextItem(
                mainText = "Задать вопрос в группу",
                label = "vk.com/kekmech"
            ))
            add(BottomLabeledTextItem(
                mainText = "Завести Issue или Pull Request",
                label = "github.com/tonykolomeytsev/mpeiapp"
            ))
        }
    }
}