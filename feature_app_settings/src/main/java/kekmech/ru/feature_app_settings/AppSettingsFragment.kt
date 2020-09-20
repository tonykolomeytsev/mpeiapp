package kekmech.ru.feature_app_settings

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.coreui.attachScrollListenerForAppBarLayoutShadow
import kekmech.ru.coreui.items.SectionHeaderAdapterItem
import kekmech.ru.feature_app_settings.di.AppSettingDependencies
import kekmech.ru.feature_app_settings.presentation.AppSettingsEffect
import kekmech.ru.feature_app_settings.presentation.AppSettingsEvent
import kekmech.ru.feature_app_settings.presentation.AppSettingsEvent.Wish
import kekmech.ru.feature_app_settings.presentation.AppSettingsFeature
import kekmech.ru.feature_app_settings.presentation.AppSettingsState
import kotlinx.android.synthetic.main.fragment_app_settings.*
import org.koin.android.ext.android.inject

class AppSettingsFragment : BaseFragment<AppSettingsEvent, AppSettingsEffect, AppSettingsState, AppSettingsFeature>() {

    override val initEvent get() = Wish.Init

    private val dependencies by inject<AppSettingDependencies>()

    override fun createFeature() = dependencies.appSettingsFeatureFactory.create()

    override var layoutId: Int = R.layout.fragment_app_settings

    private val adapter by fastLazy { createAdapter() }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.attachScrollListenerForAppBarLayoutShadow(appBarLayout)
    }

    private fun createAdapter() = BaseAdapter(
        SectionHeaderAdapterItem(itemId = SECTION_HEADER_SCHEDULE),

    )

    companion object {

        const val SECTION_HEADER_SCHEDULE = 0
    }
}