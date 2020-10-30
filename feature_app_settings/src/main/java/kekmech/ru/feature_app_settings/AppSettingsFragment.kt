package kekmech.ru.feature_app_settings

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.addSystemTopPadding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.coreui.attachScrollListenerForAppBarLayoutShadow
import kekmech.ru.coreui.items.*
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
        appBarLayout.addSystemTopPadding()
        toolbar.init(R.string.app_settings_screen_title)
    }

    override fun render(state: AppSettingsState) {
        adapter.update(AppSettingsListConverter().map(state))
    }

    override fun handleEffect(effect: AppSettingsEffect) = when (effect) {
        is AppSettingsEffect.RecreateActivity -> {
            Handler().postDelayed({ activity?.recreate() }, 200); Unit
        }
    }

    private fun createAdapter() = BaseAdapter(
        SectionHeaderAdapterItem(),
        ToggleAdapterItem(TOGGLE_DARK_THEME) { feature.accept(Wish.Action.SetDarkThemeEnabled(it)) },
        ToggleAdapterItem(TOGGLE_CHANGE_DAY_AFTER_CHANGE_WEEK) { feature.accept(Wish.Action.SetChangeDayAfterChangeWeek(it)) },
        ToggleAdapterItem(TOGGLE_AUTO_HIDE_BOTTOM_SHEET) { feature.accept(Wish.Action.SetAutoHideBottomSheet(it)) },
        SectionTextAdapterItem(),
        SpaceAdapterItem(),
        BottomLabeledTextAdapterItem { onItemClick(it.itemId) }
    )

    private fun onItemClick(itemId: Int?) = when (itemId) {
        ITEM_DEBUG_CLEAR_SELECTED_GROUP -> {
            feature.accept(Wish.Action.ClearSelectedGroup)
            dependencies.onboardingFeatureLauncher.launchWelcomePage(true)
        }
        else -> { /* no-op */ }
    }

    companion object {
        const val TOGGLE_DARK_THEME = 0
        const val TOGGLE_CHANGE_DAY_AFTER_CHANGE_WEEK = 1
        const val TOGGLE_AUTO_HIDE_BOTTOM_SHEET = 2

        const val ITEM_DEBUG_CLEAR_SELECTED_GROUP = 0
    }
}