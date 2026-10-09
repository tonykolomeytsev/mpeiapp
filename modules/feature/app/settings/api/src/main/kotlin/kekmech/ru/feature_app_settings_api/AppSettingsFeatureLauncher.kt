package kekmech.ru.feature_app_settings_api

import androidx.fragment.app.Fragment

public interface AppSettingsFeatureLauncher {

    public fun launch(subPage: SubPage? = null)

    public fun getScreen(): Fragment

    public enum class SubPage { FAVORITES }
}
