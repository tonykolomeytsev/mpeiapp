package kekmech.ru.mpeiapp.di

import kekmech.ru.feature_app_settings_impl.di.FeatureAppSettingsModule
import kekmech.ru.feature_app_update_impl.di.FeatureForceUpdateModule
import kekmech.ru.feature_bars_impl.di.FeatureBarsModule
import kekmech.ru.feature_contributors_impl.di.FeatureContributorsModule
import kekmech.ru.feature_dashboard_impl.di.FeatureDashboardModule
import kekmech.ru.feature_favorite_schedule_impl.di.FeatureFavoriteScheduleModule
import kekmech.ru.feature_main_screen_impl.di.FeatureMainScreenModule
import kekmech.ru.feature_map_impl.di.FeatureMapModule
import kekmech.ru.feature_notes_impl.di.FeatureNotesModule
import kekmech.ru.feature_onboarding_impl.di.FeatureOnboardingModule
import kekmech.ru.feature_schedule_impl.di.FeatureScheduleModule
import kekmech.ru.feature_search_impl.di.FeatureSearchFeatureModule

internal val FeatureModules = arrayOf(
    FeatureAppSettingsModule,
    FeatureBarsModule,
    FeatureDashboardModule,
    FeatureForceUpdateModule,
    FeatureMapModule,
    FeatureNotesModule,
    FeatureOnboardingModule,
    FeatureScheduleModule,
    FeatureSearchFeatureModule,
    FeatureFavoriteScheduleModule,
    FeatureContributorsModule,
    FeatureMainScreenModule,
)
