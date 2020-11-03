package kekmech.ru.feature_onboarding.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_analytics.addScrollAnalytics
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.domain_schedule.CONTINUE_TO_DASHBOARD
import kekmech.ru.feature_onboarding.R
import kekmech.ru.feature_onboarding.WelcomeScreenAnalytics
import kekmech.ru.feature_onboarding.di.OnboardingDependencies
import kekmech.ru.feature_onboarding.item.PromoPage
import kekmech.ru.feature_onboarding.item.PromoPageAdapterItem
import kotlinx.android.synthetic.main.fragment_welcome.*
import org.koin.android.ext.android.inject

internal class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private val dependencies by inject<OnboardingDependencies>()
    private val scheduleFeatureLauncher by fastLazy { dependencies.scheduleFeatureLauncher }
    private val analytics: WelcomeScreenAnalytics by inject()
    private val adapter by fastLazy { createAdapter() }
    private val snapHelper by fastLazy { PagerSnapHelper() }
    private val pages by fastLazy { PromoPage.values().asList() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonStart.setOnClickListener {
            analytics.sendClick("Start")
            scheduleFeatureLauncher.launchSearchGroup(CONTINUE_TO_DASHBOARD)
        }
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerDots.attachToRecyclerView(recycler)
        recycler.addScrollAnalytics(analytics, "PromoPager")
        snapHelper.attachToRecyclerView(recycler)
        analytics.sendScreenShown()
        adapter.update(pages)
    }

    private fun createAdapter() = BaseAdapter(
        PromoPageAdapterItem()
    )
}