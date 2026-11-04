package kekmech.ru.ui_kit_banner

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Single banner shown by the [BannerHost].
 *
 * Banners with the same [message] are still different, so showing the same message twice
 * restarts the hiding timer, like it did in the old `BannerContainer`.
 */
@Immutable
public class BannerData internal constructor(
    public val message: String,
)

/**
 * # BannerHostState
 *
 * State of the [BannerHost]. Holds the only banner which is shown right now.
 * Unlike `SnackbarHostState` there is no queue: the new banner replaces the current one.
 *
 * Usage example:
 * ```kotlin
 * val bannerHostState = remember { BannerHostState() }
 * EffectHandler(effects) { effect ->
 *     when (effect) {
 *         is Effect.ShowError -> bannerHostState.show(errorMessage)
 *     }
 * }
 * ```
 */
@Stable
public class BannerHostState {

    /**
     * The banner which is shown right now, or null if there is no banner.
     */
    public var currentBanner: BannerData? by mutableStateOf(null)
        private set

    /**
     * Show the banner with the [message]. It is hidden automatically by the [BannerHost].
     */
    public fun show(message: String) {
        currentBanner = BannerData(message)
    }

    /**
     * Hide the [banner], if it is still shown.
     */
    public fun dismiss(banner: BannerData) {
        if (currentBanner === banner) {
            currentBanner = null
        }
    }
}
