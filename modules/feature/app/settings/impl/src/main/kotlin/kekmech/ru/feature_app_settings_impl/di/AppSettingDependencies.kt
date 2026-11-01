package kekmech.ru.feature_app_settings_impl.di

import kekmech.ru.feature_app_settings_impl.presentation.screens.favorites.elm.FavoritesStoreFactory
import kekmech.ru.feature_schedule_api.ScheduleFeatureApi

internal data class AppSettingDependencies(
    val favoritesStoreFactory: FavoritesStoreFactory,
    val scheduleFeatureApi: ScheduleFeatureApi,
)
