package kekmech.ru.feature_onboarding_impl.presentation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import kekmech.ru.feature_onboarding_impl.presentation.screen.WelcomeEntry
import kekmech.ru.lib_navigation.NavEntries
import kekmech.ru.lib_navigation.ScreenKey

internal class OnboardingNavEntries : NavEntries {

    override fun EntryProviderScope<ScreenKey>.register() {
        entry<WelcomeScreenKey> { WelcomeEntry() }
    }
}
