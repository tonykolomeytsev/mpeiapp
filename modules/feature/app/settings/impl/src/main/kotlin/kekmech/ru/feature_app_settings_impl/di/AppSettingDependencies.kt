package kekmech.ru.feature_app_settings_impl.di

import kekmech.ru.feature_app_settings_impl.presentation.screens.favorites.elm.FavoritesStoreFactory
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsStoreFactory
import kekmech.ru.feature_schedule_api.ScheduleFeatureLauncher

internal data class AppSettingDependencies(
    val appSettingsStoreFactory: AppSettingsStoreFactory,
    val favoritesStoreFactory: FavoritesStoreFactory,
    val scheduleFeatureLauncher: ScheduleFeatureLauncher,
)
