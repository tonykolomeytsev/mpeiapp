package kekmech.ru.feature_app_settings_impl.presentation.screens.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kekmech.ru.coreui.attachScrollListenerForAppBarLayoutShadow
import kekmech.ru.coreui.items.BottomLabeledTextAdapterItem
import kekmech.ru.coreui.items.RightLabeledTextAdapterItem
import kekmech.ru.coreui.items.SectionHeaderAdapterItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.coreui.items.TextAdapterItem
import kekmech.ru.coreui.items.ToggleAdapterItem
import kekmech.ru.ext_android.ActivityResultListener
import kekmech.ru.ext_android.addSystemBottomPadding
import kekmech.ru.ext_android.addSystemTopPadding
import kekmech.ru.ext_android.close
import kekmech.ru.ext_android.openLinkExternal
import kekmech.ru.ext_android.setResultListener
import kekmech.ru.ext_android.viewbinding.viewBinding
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_app_settings_impl.R
import kekmech.ru.feature_app_settings_impl.databinding.FragmentAppSettingsBinding
import kekmech.ru.feature_app_settings_impl.presentation.screens.favorites.FavoritesFragment
import kekmech.ru.feature_app_settings_impl.presentation.screens.lang.SelectLanguageFragment
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsEffect
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsEvent
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsEvent.Ui
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsState
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsStoreFactory
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.list.ContributorAdapterItem
import kekmech.ru.feature_app_settings_impl.presentation.screens.map_type.SelectMapTypeFragment
import kekmech.ru.lib_adapter.BaseAdapter
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import kekmech.ru.lib_navigation.addScreenForward
import kekmech.ru.lib_navigation.showDialog
import money.vivid.elmslie.android.renderer.ElmRendererDelegate
import money.vivid.elmslie.android.renderer.androidElmStore
import org.koin.android.ext.android.inject
import kekmech.ru.res_strings.R.string as Strings

private const val ACTIVITY_RECREATION_DELAY = 200L

internal class AppSettingsFragment :
    Fragment(R.layout.fragment_app_settings),
    ElmRendererDelegate<AppSettingsEffect, AppSettingsState>,
    ActivityResultListener {

    private val adapter by fastLazy { createAdapter() }
    private val analytics by screenAnalytics("AppSettings")
    private val viewBinding by viewBinding(FragmentAppSettingsBinding::bind)

    private val store by androidElmStore { inject<AppSettingsStoreFactory>().value.create() }

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
                requireContext().openLinkExternal(it.url)
            },
        )

    private fun onItemClick(itemId: Int?) =
        when (itemId) {
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

        const val ITEM_SUPPORT = 1
        const val ITEM_GITHUB = 2
        const val ITEM_FAVORITES = 3
        const val ITEM_LANGUAGE = 4
        const val ITEM_MAP_TYPE = 5

        private const val LANGUAGE_RESULT_KEY = "LANGUAGE_RESULT_KEY"
        private const val MAP_TYPE_RESULT_KEY = "MAP_TYPE_RESULT_KEY"
    }
}
