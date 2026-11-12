package kekmech.ru.feature_dashboard_impl.data.repository

import android.content.SharedPreferences
import kekmech.ru.ext_shared_preferences.boolean

/**
 * Remembers whether the user dismissed the "What's new" banner on the dashboard
 */
internal class WhatsNewBannerRepository(
    sharedPreferences: SharedPreferences,
) {

    private var isDismissed by sharedPreferences.boolean(IS_DISMISSED_KEY)

    fun isBannerVisible(): Boolean = !isDismissed

    fun dismissBanner() {
        isDismissed = true
    }

    private companion object {

        const val IS_DISMISSED_KEY = "dashboard_whats_new_banner_dismissed_v2.4.0"
    }
}
