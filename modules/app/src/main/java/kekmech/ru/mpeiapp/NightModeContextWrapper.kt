package kekmech.ru.mpeiapp

import android.content.Context
import android.content.res.Configuration

/**
 * Dark theme is the app setting, not the system one. Replaces AppCompat's `localNightMode`:
 * the night bits of `uiMode` are overridden, so XML resources resolve `values-night` and Compose
 * screens get the right value from `isSystemInDarkTheme()`.
 *
 * Wrap the context after [LocaleContextWrapper]: the configuration is copied from the given context,
 * so the app locale is kept.
 */
object NightModeContextWrapper {

    fun wrapContext(context: Context, isDarkTheme: Boolean): Context {
        val configuration = Configuration(context.resources.configuration)
        val nightMode = if (isDarkTheme) Configuration.UI_MODE_NIGHT_YES else Configuration.UI_MODE_NIGHT_NO
        if (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == nightMode) return context
        configuration.uiMode = (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or nightMode
        return context.createConfigurationContext(configuration)
    }
}
