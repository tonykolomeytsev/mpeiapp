package kekmech.ru.feature_app_settings.screens.main

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.addSystemBottomPadding
import kekmech.ru.common_android.addSystemTopPadding
import kekmech.ru.common_android.openLinkExternal
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.addScreenForward
import kekmech.ru.coreui.attachScrollListenerForAppBarLayoutShadow
import kekmech.ru.coreui.items.*
import kekmech.ru.feature_app_settings.R
import kekmech.ru.feature_app_settings.di.AppSettingDependencies
import kekmech.ru.feature_app_settings.screens.favorites.FavoritesFragment
import kekmech.ru.feature_app_settings.screens.main.presentation.AppSettingsEffect
import kekmech.ru.feature_app_settings.screens.main.presentation.AppSettingsEvent
import kekmech.ru.feature_app_settings.screens.main.presentation.AppSettingsEvent.Wish
import kekmech.ru.feature_app_settings.screens.main.presentation.AppSettingsFeature
import kekmech.ru.feature_app_settings.screens.main.presentation.AppSettingsState
import kotlinx.android.synthetic.main.fragment_app_settings.*
import org.koin.android.ext.android.inject

internal class AppSettingsFragment : BaseFragment<AppSettingsEvent, AppSettingsEffect, AppSettingsState, AppSettingsFeature>() {

    override val initEvent get() = Wish.Init

    private val dependencies by inject<AppSettingDependencies>()

    override fun createFeature() = dependencies.appSettingsFeatureFactory.create()

    override var layoutId: Int = R.layout.fragment_app_settings

    private val adapter by fastLazy { createAdapter() }

    private val analytics by inject<AppSettingsAnalytics>()

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.attachScrollListenerForAppBarLayoutShadow(appBarLayout)
        recyclerView.addSystemBottomPadding()
        appBarLayout.addSystemTopPadding()
        toolbar.init(R.string.app_settings_screen_title)
        analytics.sendScreenShown()
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
        ToggleAdapterItem(TOGGLE_DARK_THEME) {
            analytics.sendChangeSetting("DarkTheme", it.toString())
            feature.accept(Wish.Action.SetDarkThemeEnabled(it))
        },
        ToggleAdapterItem(TOGGLE_CHANGE_DAY_AFTER_CHANGE_WEEK) {
            analytics.sendChangeSetting("ChangeDayAfterChangeWeek", it.toString())
            feature.accept(Wish.Action.SetChangeDayAfterChangeWeek(it))
        },
        ToggleAdapterItem(TOGGLE_AUTO_HIDE_BOTTOM_SHEET) {
            analytics.sendChangeSetting("AutoHideMapBottomSheet", it.toString())
            feature.accept(Wish.Action.SetAutoHideBottomSheet(it))
        },
        SectionTextAdapterItem(),
        SpaceAdapterItem(),
        BottomLabeledTextAdapterItem { onItemClick(it.itemId) }
    )

    private fun onItemClick(itemId: Int?) = when (itemId) {
        ITEM_DEBUG_CLEAR_SELECTED_GROUP -> {
            feature.accept(Wish.Action.ClearSelectedGroup)
            dependencies.onboardingFeatureLauncher.launchWelcomePage(true)
        }
        ITEM_SUPPORT -> {
            analytics.sendClick("VkGroup")
            requireContext().openLinkExternal("https://vk.com/kekmech")
        }
        ITEM_GITHUB -> {
            analytics.sendClick("GitHub")
            requireContext().openLinkExternal("https://github.com/tonykolomeytsev/mpeiapp")
        }
        ITEM_FAVORITES -> {
            analytics.sendClick("Favorites")
            addScreenForward { FavoritesFragment() }
        }
        else -> { /* no-op */ }
    }

    companion object {
        const val TOGGLE_DARK_THEME = 0
        const val TOGGLE_CHANGE_DAY_AFTER_CHANGE_WEEK = 1
        const val TOGGLE_AUTO_HIDE_BOTTOM_SHEET = 2

        const val ITEM_DEBUG_CLEAR_SELECTED_GROUP = 0
        const val ITEM_SUPPORT = 1
        const val ITEM_GITHUB = 2
        const val ITEM_FAVORITES = 3
    }
}