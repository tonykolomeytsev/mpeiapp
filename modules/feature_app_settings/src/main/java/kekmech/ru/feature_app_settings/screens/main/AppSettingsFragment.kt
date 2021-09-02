package kekmech.ru.feature_app_settings.screens.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_analytics.ext.screenAnalytics
import kekmech.ru.common_android.*
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.BaseFragment
import kekmech.ru.common_navigation.addScreenForward
import kekmech.ru.common_navigation.showDialog
import kekmech.ru.coreui.attachScrollListenerForAppBarLayoutShadow
import kekmech.ru.coreui.items.*
import kekmech.ru.feature_app_settings.R
import kekmech.ru.feature_app_settings.databinding.FragmentAppSettingsBinding
import kekmech.ru.feature_app_settings.di.AppSettingDependencies
import kekmech.ru.feature_app_settings.screens.favorites.FavoritesFragment
import kekmech.ru.feature_app_settings.screens.lang.SelectLanguageFragment
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsEffect
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsEvent
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsEvent.Wish
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsFeatureFactory
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsState
import org.koin.android.ext.android.inject

private const val RESULT_SELECTED_LANG = 846583
private const val ACTIVITY_RECREATION_DELAY = 200L

internal class AppSettingsFragment :
    BaseFragment<AppSettingsEvent, AppSettingsEffect, AppSettingsState>(),
    ActivityResultListener {

    override val initEvent get() = Wish.Init

    override var layoutId: Int = R.layout.fragment_app_settings

    private val dependencies by inject<AppSettingDependencies>()
    private val adapter by fastLazy { createAdapter() }
    private val analytics by screenAnalytics("AppSettings")
    private val viewBinding by viewBinding(FragmentAppSettingsBinding::bind)

    override fun createStore() = inject<AppSettingsFeatureFactory>().value.create()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            recyclerView.attachScrollListenerForAppBarLayoutShadow(appBarLayout)
            recyclerView.addSystemBottomPadding()
            appBarLayout.addSystemTopPadding()
            toolbar.setTitle(R.string.app_settings_screen_title)
            toolbar.setNavigationOnClickListener { close() }
        }
    }

    override fun render(state: AppSettingsState) {
        adapter.update(AppSettingsListConverter().map(state, requireContext()))
    }

    override fun handleEffect(effect: AppSettingsEffect) = when (effect) {
        is AppSettingsEffect.RecreateActivity -> recreateActivity()
        is AppSettingsEffect.OpenLanguageSelectionDialog -> showDialog {
            SelectLanguageFragment.newInstance(effect.selectedLanguage)
                .withResultFor(this, RESULT_SELECTED_LANG)
        }
    }

    private fun createAdapter() = BaseAdapter(
        SectionHeaderAdapterItem(),
        ToggleAdapterItem(TOGGLE_DARK_THEME) {
            analytics.sendChangeSetting("DarkTheme", it.toString())
            feature.accept(Wish.Action.SetDarkThemeEnabled(it))
            fixStatusBarIssue(it)
        },
        ToggleAdapterItem(TOGGLE_AUTO_HIDE_BOTTOM_SHEET) {
            analytics.sendChangeSetting("AutoHideMapBottomSheet", it.toString())
            feature.accept(Wish.Action.SetAutoHideBottomSheet(it))
        },
        ToggleAdapterItem(TOGGLE_SNOW_FLAKES) {
            analytics.sendChangeSetting("SnowFlakes", it.toString())
            feature.accept(Wish.Action.SetSnowEnabled(it))
        },
        ToggleAdapterItem(TOGGLE_SHOW_NAV_FAB) {
            analytics.sendChangeSetting("ShowQuickNavFab", it.toString())
            feature.accept(Wish.Action.SetShowQuickNavigationFab(it))
        },
        ToggleAdapterItem(TOGGLE_DEBUG_CHANGE_ENV) {
            feature.accept(Wish.Action.ChangeBackendEnvironment(it))
        },
        SectionTextAdapterItem(),
        SpaceAdapterItem(),
        BottomLabeledTextAdapterItem { onItemClick(it.itemId) },
        RightLabeledTextAdapterItem { onItemClick(it.itemId) },
        TextAdapterItem(R.layout.item_app_version)
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
        ITEM_LANGUAGE -> {
            analytics.sendClick("SelectLanguage")
            feature.accept(Wish.Click.OnLanguage)
        }
        else -> { /* no-op */ }
    }

    @Suppress("DEPRECATION") // потому что это фикс для старых версий
    private fun fixStatusBarIssue(isDarkThemeEnabled: Boolean) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val decorView = requireActivity().window.decorView
            val oldSystemUiVisibility = decorView.systemUiVisibility
            if (isDarkThemeEnabled) {
                decorView.systemUiVisibility = oldSystemUiVisibility xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = oldSystemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    private fun recreateActivity() {
        Handler(Looper.getMainLooper())
            .postDelayed({ activity?.recreate() }, ACTIVITY_RECREATION_DELAY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RESULT_SELECTED_LANG && data != null) {
            val selectedLanguage = data.getStringExtra("app_lang") ?: return
            feature.accept(Wish.Action.LanguageChanged(selectedLanguage))
        }
    }

    companion object {
        const val TOGGLE_DARK_THEME = 0
        const val TOGGLE_AUTO_HIDE_BOTTOM_SHEET = 2
        const val TOGGLE_SNOW_FLAKES = 3
        const val TOGGLE_DEBUG_CHANGE_ENV = 4
        const val TOGGLE_SHOW_NAV_FAB = 5

        const val ITEM_DEBUG_CLEAR_SELECTED_GROUP = 0
        const val ITEM_SUPPORT = 1
        const val ITEM_GITHUB = 2
        const val ITEM_FAVORITES = 3
        const val ITEM_LANGUAGE = 4
    }
}