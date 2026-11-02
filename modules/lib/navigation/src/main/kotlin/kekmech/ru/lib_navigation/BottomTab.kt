package kekmech.ru.lib_navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavEntry
import kotlinx.parcelize.Parcelize
import java.io.Serializable

public enum class BottomTab : Serializable {
    DASHBOARD, SCHEDULE, MAP, PROFILE
}

/**
 * Ключи корневых экранов табов. Лежат здесь, а не в фичах, потому что back stack
 * при переключении табов собирает приложение, а экраны для них регистрируют фичи.
 */
public sealed interface TabScreenKey : ScreenKey {
    public val tab: BottomTab
}

@Parcelize
public data object DashboardTabKey : TabScreenKey {
    override val tab: BottomTab get() = BottomTab.DASHBOARD
}

@Parcelize
public data object ScheduleTabKey : TabScreenKey {
    override val tab: BottomTab get() = BottomTab.SCHEDULE
}

@Parcelize
public data object MapTabKey : TabScreenKey {
    override val tab: BottomTab get() = BottomTab.MAP
}

@Parcelize
public data object ProfileTabKey : TabScreenKey {
    override val tab: BottomTab get() = BottomTab.PROFILE
}

public val BottomTab.screenKey: TabScreenKey
    get() = when (this) {
        BottomTab.DASHBOARD -> DashboardTabKey
        BottomTab.SCHEDULE -> ScheduleTabKey
        BottomTab.MAP -> MapTabKey
        BottomTab.PROFILE -> ProfileTabKey
    }

private const val BOTTOM_TAB_METADATA_KEY = "mpeix.bottomTab"

/**
 * Регистрирует корневой экран таба. Приложение рисует нижнюю навигацию только вокруг таких
 * экранов, а `NavEntry` не отдает свой ключ наружу, поэтому таб передается через metadata.
 */
public fun EntryProviderScope<ScreenKey>.tabEntry(
    key: TabScreenKey,
    content: @Composable () -> Unit,
) {
    entry(key, metadata = mapOf(BOTTOM_TAB_METADATA_KEY to key.tab)) { content() }
}

public val NavEntry<*>.bottomTab: BottomTab?
    get() = metadata[BOTTOM_TAB_METADATA_KEY] as? BottomTab
