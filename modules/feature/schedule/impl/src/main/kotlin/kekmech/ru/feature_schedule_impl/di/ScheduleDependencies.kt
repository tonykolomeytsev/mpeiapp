package kekmech.ru.feature_schedule_impl.di

import kekmech.ru.feature_main_screen_api.MainScreenLauncher
import kekmech.ru.feature_notes_api.NotesFeatureLauncher
import kekmech.ru.feature_onboarding_api.OnboardingFeatureApi

internal data class ScheduleDependencies(
    val onboardingFeatureApi: OnboardingFeatureApi,
    val notesFeatureLauncher: NotesFeatureLauncher,
    val mainScreenLauncher: MainScreenLauncher
)
