package kekmech.ru.feature_schedule_impl.di

import kekmech.ru.feature_main_screen_api.MainScreenLauncher
import kekmech.ru.feature_notes_api.NotesFeatureLauncher
import kekmech.ru.feature_onboarding_api.OnboardingFeatureLauncher

internal data class ScheduleDependencies(
    val onboardingFeatureLauncher: OnboardingFeatureLauncher,
    val notesFeatureLauncher: NotesFeatureLauncher,
    val mainScreenLauncher: MainScreenLauncher
)
