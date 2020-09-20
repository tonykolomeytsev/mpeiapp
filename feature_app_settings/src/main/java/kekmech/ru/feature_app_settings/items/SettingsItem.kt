package kekmech.ru.feature_app_settings.items

import androidx.annotation.LayoutRes


data class SettingsItem(
    @LayoutRes val titleRes: Int,
    @LayoutRes val subtitleRes: Int,
    val type: SettingsItemType = SettingsItemType.TOGGLE,
    val scope: SettingsItemScope = SettingsItemScope.SCHEDULE,
    val itemId: Int = 0
)

enum class SettingsItemType { TOGGLE }

enum class SettingsItemScope { SCHEDULE }
