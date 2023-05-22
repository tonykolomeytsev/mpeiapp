package kekmech.ru.feature_app_settings.screens.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_analytics.ext.screenAnalytics
import kekmech.ru.common_android.*
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_elm.BaseFragment
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_navigation.addScreenForward
import kekmech.ru.common_navigation.showDialog
import kekmech.ru.coreui.attachScrollListenerForAppBarLayoutShadow
import kekmech.ru.coreui.items.*
import kekmech.ru.domain_app_settings_models.AppEnvironment
import kekmech.ru.feature_app_settings.R
import kekmech.ru.feature_app_settings.databinding.FragmentAppSettingsBinding
import kekmech.ru.feature_app_settings.di.AppSettingDependencies
import kekmech.ru.feature_app_settings.screens.favorites.FavoritesFragment
import kekmech.ru.feature_app_settings.screens.lang.SelectLanguageFragment
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsEffect
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsEvent
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsEvent.Ui
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsFeatureFactory
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsState
import kekmech.ru.feature_app_settings.screens.main.list.ContributorAdapterItem
import kekmech.ru.feature_app_settings.screens.map_type.SelectMapTypeFragment
import kekmech.ru.strings.Strings
import org.koin.android.ext.android.inject

private const val ACTIVITY_RECREATION_DELAY = 200L

internal class AppSettingsFragment :
    BaseFragment<AppSettingsEvent, AppSettingsEffect, AppSettingsState>(R.layout.fragment_app_settings),
    ActivityResultListener {

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
            recyclerView.disablePicassoLoadingOnScroll()
            appBarLayout.addSystemTopPadding()
            toolbar.setTitle(Strings.app_settings_screen_title)
            toolbar.setNavigationOnClickListener { close() }
        }
    }

    override fun render(state: AppSettingsState) {
        adapter.update(AppSettingsListConverter().map(state, requireContext()))
    }

    override fun handleEffect(effect: AppSettingsEffect) =
        when (effect) {
            is AppSettingsEffect.RecreateActivity -> recreateActivity()
            is AppSettingsEffect.OpenLanguageSelectionDialog -> {
                showDialog {
                    SelectLanguageFragment.newInstance(effect.selectedLanguage, LANGUAGE_RESULT_KEY)
                }
                setResultListener<String>(LANGUAGE_RESULT_KEY) { selectedLanguage ->
                    store.accept(Ui.Action.LanguageChanged(selectedLanguage))
                }
            }
            is AppSettingsEffect.OpenMapTypeDialog -> {
                showDialog {
                    SelectMapTypeFragment.newInstance(effect.mapType, MAP_TYPE_RESULT_KEY)
                }
                setResultListener<String>(MAP_TYPE_RESULT_KEY) { selectedMapType ->
                    store.accept(Ui.Action.MapTypeChanged(selectedMapType))
                }
            }
        }

    private fun createAdapter() =
        BaseAdapter(
            SectionHeaderAdapterItem(),
            ToggleAdapterItem(TOGGLE_DARK_THEME) {
                analytics.sendChangeSetting("DarkTheme", it.toString())
                store.accept(Ui.Action.SetDarkThemeEnabled(it))
                fixStatusBarIssue(it)
            },
            ToggleAdapterItem(TOGGLE_AUTO_HIDE_BOTTOM_SHEET) {
                analytics.sendChangeSetting("AutoHideMapBottomSheet", it.toString())
                store.accept(Ui.Action.SetAutoHideBottomSheet(it))
            },
            ToggleAdapterItem(TOGGLE_SNOW_FLAKES) {
                analytics.sendChangeSetting("SnowFlakes", it.toString())
                store.accept(Ui.Action.SetSnowEnabled(it))
            },
            ToggleAdapterItem(TOGGLE_SHOW_NAV_FAB) {
                analytics.sendChangeSetting("ShowQuickNavFab", it.toString())
                store.accept(Ui.Action.SetShowQuickNavigationFab(it))
            },
            TextAdapterItem(),
            SpaceAdapterItem(),
            BottomLabeledTextAdapterItem { onItemClick(it.itemId) },
            RightLabeledTextAdapterItem { onItemClick(it.itemId) },
            ContributorAdapterItem {
                requireContext().openLinkExternal(it.gitHubPageUrl)
            },
        )

    private fun onItemClick(itemId: Int?) =
        when (itemId) {
            ITEM_DEBUG_CLEAR_SELECTED_GROUP -> {
                store.accept(Ui.Action.ClearSelectedGroup)
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
                store.accept(Ui.Click.Language)
            }
            ITEM_MAP_TYPE -> {
                analytics.sendClick("SelectMapType")
                store.accept(Ui.Click.MapType)
            }
            ITEM_DEBUG_SELECT_ENVIRONMENT -> {
                showEnvironmentSelectorDialog()
            }
            else -> { /* no-op */
            }
        }

    @Suppress("DEPRECATION") // потому что это фикс для старых версий
    private fun fixStatusBarIssue(isDarkThemeEnabled: Boolean) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val decorView = requireActivity().window.decorView
            val oldSystemUiVisibility = decorView.systemUiVisibility
            if (isDarkThemeEnabled) {
                decorView.systemUiVisibility =
                    oldSystemUiVisibility xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility =
                    oldSystemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    private fun recreateActivity() {
        Handler(Looper.getMainLooper())
            .postDelayed({ activity?.recreate() }, ACTIVITY_RECREATION_DELAY)
    }

    private fun showEnvironmentSelectorDialog() {
        val environments = AppEnvironment.values().map(Any::toString).toTypedArray()
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Выбрать окружение")
            .setItems(environments) { dialog, which ->
                dialog?.dismiss()
                store.accept(
                    Ui.Action.ChangeBackendEnvironment(
                        AppEnvironment.valueOf(environments[which])
                    )
                )
            }
            .show()
    }

    private fun RecyclerView.disablePicassoLoadingOnScroll() =
        addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> Picasso.get()
                        .resumeTag(ContributorAdapterItem::class)
                    else -> Picasso.get().pauseTag(ContributorAdapterItem::class)
                }
            }
        })

    companion object {

        const val TOGGLE_DARK_THEME = 0
        const val TOGGLE_AUTO_HIDE_BOTTOM_SHEET = 2
        const val TOGGLE_SNOW_FLAKES = 3
        const val TOGGLE_SHOW_NAV_FAB = 4

        const val ITEM_DEBUG_CLEAR_SELECTED_GROUP = 0
        const val ITEM_SUPPORT = 1
        const val ITEM_GITHUB = 2
        const val ITEM_FAVORITES = 3
        const val ITEM_LANGUAGE = 4
        const val ITEM_MAP_TYPE = 5
        const val ITEM_DEBUG_SELECT_ENVIRONMENT = 6

        private const val LANGUAGE_RESULT_KEY = "LANGUAGE_RESULT_KEY"
        private const val MAP_TYPE_RESULT_KEY = "MAP_TYPE_RESULT_KEY"
    }
}
