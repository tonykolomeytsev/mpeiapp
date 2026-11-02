package kekmech.ru.feature_app_update_impl.navigation

import kekmech.ru.feature_app_update_impl.domain.models.ForceUpdateInfo
import kekmech.ru.lib_navigation.ScreenKey
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class BlockingUpdateScreenKey(val updateInfo: ForceUpdateInfo) : ScreenKey
