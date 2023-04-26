package kekmech.ru.feature_app_settings.screens.main

import android.content.Context
import android.view.Gravity
import androidx.annotation.StringRes
import kekmech.ru.coreui.items.*
import kekmech.ru.feature_app_settings.BuildConfig
import kekmech.ru.feature_app_settings.screens.main.AppSettingsFragment.Companion.ITEM_GITHUB
import kekmech.ru.feature_app_settings.screens.main.AppSettingsFragment.Companion.ITEM_SUPPORT
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsState
import kekmech.ru.strings.Strings
import kekmech.ru.coreui.R as coreui_R

internal class AppSettingsListConverter {

    @Suppress("LongMethod")
    fun map(state: AppSettingsState, context: Context): List<Any> {
        val appSettings = state.appSettings ?: return emptyList()
        return mutableListOf<Any>().apply {

            addToggleWithDescription(
                itemId = AppSettingsFragment.TOGGLE_DARK_THEME,
                toggleTextResId = Strings.app_settings_dark_theme,
                isChecked = appSettings.isDarkThemeEnabled,
                descriptionResId = Strings.app_settings_dark_theme_description
            )

            addSwitchLanguageItem(state)

            if (state.isFeatureToggleSnowFlakesEnabled) {
                add(ToggleItem(
                    itemId = AppSettingsFragment.TOGGLE_SNOW_FLAKES,
                    titleRes = Strings.app_settings_snowy_mood,
                    isChecked = appSettings.isSnowEnabled
                ))
            }

            addSection(titleResId = Strings.app_settings_header_schedule) {

                add(BottomLabeledTextItem(
                    itemId = AppSettingsFragment.ITEM_FAVORITES,
                    mainTextResId = Strings.app_settings_favorite_schedule_title,
                    labelResId = Strings.app_settings_favorite_schedule_description
                ))

                addToggleWithDescription(
                    itemId = AppSettingsFragment.TOGGLE_SHOW_NAV_FAB,
                    toggleTextResId = Strings.app_settings_show_navigation_fab,
                    isChecked = appSettings.showNavigationButton,
                    descriptionResId = Strings.app_settings_show_navigation_fab_description
                )
            }

            addSection(titleResId = Strings.app_settings_header_map) {

                addToggleWithDescription(
                    itemId = AppSettingsFragment.TOGGLE_AUTO_HIDE_BOTTOM_SHEET,
                    toggleTextResId = Strings.app_settings_auto_hide_bottom_sheet,
                    isChecked = appSettings.autoHideBottomSheet,
                    descriptionResId = Strings.app_settings_auto_hide_bottom_sheet_description
                )

                addSwitchMapTypeItem(state, context)
            }

            addSection(titleResId = Strings.app_settings_header_support) {

                add(BottomLabeledTextItem(
                    mainTextResId = Strings.app_settings_section_ask,
                    label = "vk.com/kekmech",
                    itemId = ITEM_SUPPORT,
                ))

                add(BottomLabeledTextItem(
                    mainTextResId = Strings.app_settings_section_github,
                    label = "github.com/tonykolomeytsev/mpeiapp",
                    itemId = ITEM_GITHUB,
                ))
            }

            if (state.contributors != null) addSection(titleResId = Strings.app_settings_header_contributors) {
                addAll(state.contributors)
            }

            if (BuildConfig.DEBUG) {
                add(SpaceItem.VERTICAL_24)
                add(SectionHeaderItem(title = "Дебаг"))
                add(SpaceItem.VERTICAL_12)
                add(BottomLabeledTextItem(
                    mainText = "Очистить поле selected_group",
                    label = "После очистки будет открыт онбординг",
                    itemId = AppSettingsFragment.ITEM_DEBUG_CLEAR_SELECTED_GROUP,
                ))
                add(RightLabeledTextItem(
                    mainTextResId = Strings.app_settings_section_lang,
                    label = when (state.appSettings.languageCode) {
                        "ru_RU" -> "РУССКИЙ"
                        "en_US" -> "ENGLISH"
                        else -> ""
                    },
                    itemId = AppSettingsFragment.ITEM_LANGUAGE,
                ))
                add(RightLabeledTextItem(
                    mainText = "Окружение приложения",
                    label = state.appSettings.appEnvironment.toString(),
                    itemId = AppSettingsFragment.ITEM_DEBUG_SELECT_ENVIRONMENT,
                ))
                add(TextItem(
                    text = "Нужно перезагрузить приложение, для того чтобы настройка вступила в силу",
                    styleResId = coreui_R.style.H8_Gray70_Medium,
                ))
            }

            add(SpaceItem.VERTICAL_24)
            add(getVersionItem(context))
            add(SpaceItem.VERTICAL_24)
        }
    }

    private fun getVersionItem(context: Context): Any {
        val versionName = BuildConfig.VERSION_NAME
        return TextItem(
            text = context.getString(Strings.app_settings_app_version, versionName),
            styleResId = coreui_R.style.H6_Gray70,
            textGravity = Gravity.CENTER
        )
    }

    private fun MutableList<Any>.addSwitchLanguageItem(state: AppSettingsState) {
        add(RightLabeledTextItem(
            mainTextResId = Strings.app_settings_section_lang,
            label = when (state.appSettings?.languageCode) {
                "ru_RU" -> "РУССКИЙ"
                "en_US" -> "ENGLISH"
                else -> ""
            },
            itemId = AppSettingsFragment.ITEM_LANGUAGE
        ))
        add(TextItem(
            textResId = Strings.app_settings_section_lang_description,
            styleResId = coreui_R.style.H8_Gray70_Medium
        ))
    }

    private fun MutableList<Any>.addSwitchMapTypeItem(state: AppSettingsState, context: Context) {
        add(RightLabeledTextItem(
            mainTextResId = Strings.app_settings_map_appearance_type,
            label = when (state.appSettings?.mapAppearanceType) {
                "hybrid" -> context.getString(Strings.change_map_type_hybrid).uppercase()
                "scheme" -> context.getString(Strings.change_map_type_scheme).uppercase()
                else -> ""
            },
            itemId = AppSettingsFragment.ITEM_MAP_TYPE
        ))
    }

    private fun MutableList<Any>.addToggleWithDescription(
        itemId: Int,
        @StringRes toggleTextResId: Int,
        isChecked: Boolean,
        @StringRes descriptionResId: Int,
    ) {
        add(ToggleItem(itemId = itemId, titleRes = toggleTextResId, isChecked = isChecked))
        add(TextItem(textResId = descriptionResId, styleResId = coreui_R.style.H8_Gray70_Medium))
    }

    private fun MutableList<Any>.addSection(
        @StringRes titleResId: Int,
        content: MutableList<Any>.() -> Unit,
    ) {
        add(SpaceItem.VERTICAL_24)
        add(SectionHeaderItem(titleRes = titleResId))
        add(SpaceItem.VERTICAL_8)
        content.invoke(this)
    }
}
