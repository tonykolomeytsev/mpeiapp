package kekmech.ru.feature_onboarding.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.domain_schedule.CONTINUE_TO_BARS_ONBOARDING
import kekmech.ru.feature_onboarding.R
import kekmech.ru.feature_onboarding.WelcomeScreenAnalytics
import kekmech.ru.feature_onboarding.di.OnboardingDependencies
import kotlinx.android.synthetic.main.fragment_welcome.*
import org.koin.android.ext.android.inject

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private val dependencies by inject<OnboardingDependencies>()
    private val scheduleFeatureLauncher by fastLazy { dependencies.scheduleFeatureLauncher }
    private val analytics: WelcomeScreenAnalytics by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonStart.setOnClickListener {
            analytics.sendClick("Start")
            scheduleFeatureLauncher.launchSearchGroup(CONTINUE_TO_BARS_ONBOARDING)
        }
        analytics.sendScreenShown()
    }
}