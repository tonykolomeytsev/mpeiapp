package kekmech.ru.feature_schedule.di

import kekmech.ru.domain_main_screen.MainScreenLauncher
import kekmech.ru.domain_notes.NotesFeatureLauncher
import kekmech.ru.domain_onboarding.OnboardingFeatureLauncher

internal data class ScheduleDependencies(
    val onboardingFeatureLauncher: OnboardingFeatureLauncher,
    val notesFeatureLauncher: NotesFeatureLauncher,
    val mainScreenLauncher: MainScreenLauncher
)