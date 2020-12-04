package kekmech.ru.feature_app_settings.screens.main

import kekmech.ru.coreui.items.*
import kekmech.ru.feature_app_settings.BuildConfig
import kekmech.ru.feature_app_settings.R
import kekmech.ru.feature_app_settings.screens.main.AppSettingsFragment.Companion.ITEM_GITHUB
import kekmech.ru.feature_app_settings.screens.main.AppSettingsFragment.Companion.ITEM_SUPPORT
import kekmech.ru.feature_app_settings.screens.main.presentation.AppSettingsState

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
            add(createLanguageItem(state))
            add(SectionTextItem(resId = R.string.app_settings_section_lang_description))
            if (state.isFeatureToggleSnowFlakesEnabled) {
                add(ToggleItem(
                    itemId = AppSettingsFragment.TOGGLE_SNOW_FLAKES,
                    titleRes = R.string.app_settings_snowy_mood,
                    isChecked = appSettings.isSnowEnabled
                ))
            }
            add(SpaceItem.VERTICAL_24)

            add(SectionHeaderItem(
                titleRes = R.string.app_settings_header_schedule
            ))
            add(SpaceItem.VERTICAL_8)
            add(BottomLabeledTextItem(
                itemId = AppSettingsFragment.ITEM_FAVORITES,
                mainTextResId = R.string.app_settings_favorite_schedule_title,
                labelResId = R.string.app_settings_favorite_schedule_description
            ))
            add(ToggleItem(
                itemId = AppSettingsFragment.TOGGLE_SHOW_NAV_FAB,
                titleRes = R.string.app_settings_show_navigation_fab,
                isChecked = appSettings.showNavigationButton
            ))
            add(SectionTextItem(resId = R.string.app_settings_show_navigation_fab_description))
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
            add(SectionHeaderItem(titleRes = R.string.app_settings_header_support))
            add(SpaceItem.VERTICAL_12)
            add(BottomLabeledTextItem(
                mainTextResId = R.string.app_settings_section_ask,
                label = "vk.com/kekmech",
                itemId = ITEM_SUPPORT
            ))
            add(BottomLabeledTextItem(
                mainTextResId = R.string.app_settings_section_github,
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
                add(ToggleItem(
                    itemId = AppSettingsFragment.TOGGLE_DEBUG_CHANGE_ENV,
                    title = "Использовать тестовое окружение",
                    isChecked = appSettings.isDebugEnvironment
                ))
                add(SectionTextItem(text = "Нужно перезагрузить приложение, для того чтобы настройка вступила в силу"))
            }
        }
    }

    private fun createLanguageItem(state: AppSettingsState): Any {
        return RightLabeledTextItem(
            mainTextResId = R.string.app_settings_section_lang,
            label = when (state.appSettings?.languageCode) {
                "ru_RU" -> "РУССКИЙ"
                "en_US" -> "ENGLISH"
                else -> ""
            },
            itemId = AppSettingsFragment.ITEM_LANGUAGE
        )
    }
}