package kekmech.ru.feature_app_settings_impl.presentation.screens.main

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.runtime.Composable
import com.kekmech.lib_fragment.ComposeFragment
import com.kekmech.lib_fragment.LocalRouter
import com.kekmech.lib_fragment.ResultListener
import kekmech.ru.ext_android.openLinkExternal
import kekmech.ru.feature_app_settings_impl.presentation.screens.favorites.FavoritesFragment
import kekmech.ru.feature_app_settings_impl.presentation.screens.lang.SelectLanguageFragment
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsEffect
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsEvent
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsEvent.Ui
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsStoreFactory
import kekmech.ru.feature_app_settings_impl.presentation.screens.map_type.SelectMapTypeFragment
import kekmech.ru.feature_bars_api.BarsLogoutHandle
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import kekmech.ru.lib_elm_compose.EffectHandler
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.lib_navigation.AddScreenForward
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.lib_navigation.Router
import kekmech.ru.lib_navigation.ShowDialog
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.android.ext.android.inject
import org.koin.compose.koinInject

private const val ACTIVITY_RECREATION_DELAY = 200L
private const val LINK_GITHUB = "https://github.com/tonykolomeytsev/mpeiapp"

internal class AppSettingsFragment : ComposeFragment() {

    private val analytics by screenAnalytics("AppSettings")
    private val logoutHandle by inject<BarsLogoutHandle>()

    @Composable
    override fun Screen() {
        MpeixTheme {
            val storeFactory = koinInject<AppSettingsStoreFactory>()
            val router = LocalRouter.current
            ElmContent(
                key = "AppSettingsScreen",
                storeFactory = { storeFactory.create() },
            ) { onAccept, state, effects ->
                ResultListener<String>(LANGUAGE_RESULT_KEY) {
                    onAccept(Ui.Action.LanguageChanged(it))
                }
                ResultListener<String>(MAP_TYPE_RESULT_KEY) {
                    onAccept(Ui.Action.MapTypeChanged(it))
                }
                EffectHandler(effects) { effect ->
                    handleEffect(effect, router)
                }
                AppSettingsScreen(
                    state = state,
                    onAction = { handleAction(it, onAccept, router) },
                )
            }
        }
    }

    @Deprecated("Migrate to compose fully")
    private fun handleEffect(effect: AppSettingsEffect, router: Router) =
        when (effect) {
            is AppSettingsEffect.RecreateActivity -> recreateActivity()
            is AppSettingsEffect.OpenLanguageSelectionDialog -> router.executeCommand(ShowDialog {
                SelectLanguageFragment.newInstance(effect.selectedLanguage, LANGUAGE_RESULT_KEY)
            })

            is AppSettingsEffect.OpenMapTypeDialog -> router.executeCommand(ShowDialog {
                SelectMapTypeFragment.newInstance(effect.mapType, MAP_TYPE_RESULT_KEY)
            })
        }

    @Suppress("CyclomaticComplexMethod")
    @Deprecated("Migrate to compose fully")
    private fun handleAction(
        action: AppSettingsAction,
        onAccept: (AppSettingsEvent) -> Unit,
        router: Router,
    ) {
        when (action) {
            is AppSettingsAction.BackClick -> router.executeCommand(PopBackStack())
            is AppSettingsAction.DarkThemeChange -> {
                analytics.sendChangeSetting("DarkTheme", action.isEnabled.toString())
                onAccept(Ui.Action.SetDarkThemeEnabled(action.isEnabled))
            }

            is AppSettingsAction.AutoHideBottomSheetChange -> {
                analytics.sendChangeSetting("AutoHideMapBottomSheet", action.isEnabled.toString())
                onAccept(Ui.Action.SetAutoHideBottomSheet(action.isEnabled))
            }

            is AppSettingsAction.SnowChange -> {
                analytics.sendChangeSetting("SnowFlakes", action.isEnabled.toString())
                onAccept(Ui.Action.SetSnowEnabled(action.isEnabled))
            }

            is AppSettingsAction.QuickNavigationFabChange -> {
                analytics.sendChangeSetting("ShowQuickNavFab", action.isVisible.toString())
                onAccept(Ui.Action.SetShowQuickNavigationFab(action.isVisible))
            }

            is AppSettingsAction.GitHubClick -> {
                analytics.sendClick("GitHub")
                requireContext().openLinkExternal(LINK_GITHUB)
            }

            is AppSettingsAction.FavoritesClick -> {
                analytics.sendClick("Favorites")
                router.executeCommand(AddScreenForward { FavoritesFragment() })
            }

            is AppSettingsAction.LanguageClick -> {
                analytics.sendClick("SelectLanguage")
                onAccept(Ui.Click.Language)
            }

            is AppSettingsAction.MapTypeClick -> {
                analytics.sendClick("SelectMapType")
                onAccept(Ui.Click.MapType)
            }

            is AppSettingsAction.BarsLogoutClick -> {
                analytics.sendClick("BarsLogout")
                if (logoutHandle.logout()) {
                    Toast
                        .makeText(requireContext(), "Вы вышли из БАРС", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            is AppSettingsAction.ContributorClick ->
                requireContext().openLinkExternal(action.contributor.url)
        }
    }

    private fun recreateActivity() {
        Handler(Looper.getMainLooper())
            .postDelayed({ activity?.recreate() }, ACTIVITY_RECREATION_DELAY)
    }

    private companion object {

        const val LANGUAGE_RESULT_KEY = "LANGUAGE_RESULT_KEY"
        const val MAP_TYPE_RESULT_KEY = "MAP_TYPE_RESULT_KEY"
    }
}
