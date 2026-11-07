package kekmech.ru.feature_app_settings_impl.presentation.screens.main

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.kekmech.lib_fragment.LocalRouter
import com.kekmech.lib_fragment.ResultListener
import kekmech.ru.ext_android.openLinkExternal
import kekmech.ru.feature_app_settings_impl.navigation.FavoritesScreenKey
import kekmech.ru.feature_app_settings_impl.presentation.screens.lang.SelectLanguageFragment
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsEffect
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsEvent
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsEvent.Ui
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsStoreFactory
import kekmech.ru.feature_app_settings_impl.presentation.screens.map_type.SelectMapTypeFragment
import kekmech.ru.feature_bars_api.BarsLogoutHandle
import kekmech.ru.lib_analytics_android.Analytics
import kekmech.ru.lib_analytics_android.ext.rememberScreenAnalytics
import kekmech.ru.lib_elm_compose.EffectHandler
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.lib_navigation.AddScreenForward
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.lib_navigation.Router
import kekmech.ru.lib_navigation.ShowDialog
import org.koin.compose.koinInject

private const val ACTIVITY_RECREATION_DELAY = 200L
private const val LINK_GITHUB = "https://github.com/tonykolomeytsev/mpeiapp"
private const val LANGUAGE_RESULT_KEY = "LANGUAGE_RESULT_KEY"
private const val MAP_TYPE_RESULT_KEY = "MAP_TYPE_RESULT_KEY"

@Composable
internal fun AppSettingsEntry() {
    val analytics = rememberScreenAnalytics("AppSettings")
    val logoutHandle = koinInject<BarsLogoutHandle>()
    val context = LocalContext.current
    val activity = LocalActivity.current
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
            handleEffect(effect, router, activity)
        }
        AppSettingsScreen(
            state = state,
            onAction = {
                handleAction(it, onAccept, router, analytics, logoutHandle, context)
            },
        )
    }
}

@Deprecated("Migrate to compose fully")
private fun handleEffect(effect: AppSettingsEffect, router: Router, activity: Activity?) =
    when (effect) {
        is AppSettingsEffect.RecreateActivity -> recreateActivity(activity)
        is AppSettingsEffect.OpenLanguageSelectionDialog -> router.executeCommand(ShowDialog {
            SelectLanguageFragment.newInstance(effect.selectedLanguage, LANGUAGE_RESULT_KEY)
        })

        is AppSettingsEffect.OpenMapTypeDialog -> router.executeCommand(ShowDialog {
            SelectMapTypeFragment.newInstance(effect.mapType, MAP_TYPE_RESULT_KEY)
        })
    }

@Suppress("CyclomaticComplexMethod", "LongParameterList")
@Deprecated("Migrate to compose fully")
private fun handleAction(
    action: AppSettingsAction,
    onAccept: (AppSettingsEvent) -> Unit,
    router: Router,
    analytics: Analytics,
    logoutHandle: BarsLogoutHandle,
    context: Context,
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
            context.openLinkExternal(LINK_GITHUB)
        }

        is AppSettingsAction.FavoritesClick -> {
            analytics.sendClick("Favorites")
            router.executeCommand(AddScreenForward(FavoritesScreenKey))
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
                    .makeText(context, "Вы вышли из БАРС", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        is AppSettingsAction.ContributorClick ->
            context.openLinkExternal(action.contributor.url)
    }
}

private fun recreateActivity(activity: Activity?) {
    Handler(Looper.getMainLooper())
        .postDelayed({ activity?.recreate() }, ACTIVITY_RECREATION_DELAY)
}
