package kekmech.ru.mpeiapp.testing

import android.content.Context
import android.content.SharedPreferences

// The preferences the app keeps its settings in, see FeatureAppSettingsModule
private const val AppPreferencesName = "mpeix"

/**
 * Writes the app settings the way the app stores them.
 * The write is synchronous, so the values are there before the app reads them.
 */
fun Context.editAppPreferences(edit: SharedPreferences.Editor.() -> Unit) {
    val isWritten = getSharedPreferences(AppPreferencesName, Context.MODE_PRIVATE)
        .edit()
        .apply(edit)
        .commit()
    check(isWritten) { "Failed to write the app preferences" }
}
