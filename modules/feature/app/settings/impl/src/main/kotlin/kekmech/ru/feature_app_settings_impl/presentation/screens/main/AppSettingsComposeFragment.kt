package kekmech.ru.feature_app_settings_impl.presentation.screens.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import com.kekmech.lib_fragment.ComposeFragment
import kekmech.ru.ext_android.close
import kekmech.ru.ext_android.notNullString
import kekmech.ru.ext_android.openLinkExternal
import kekmech.ru.ext_android.setResultListener
import kekmech.ru.feature_app_settings_impl.presentation.screens.favorites.FavoritesFragment
import kekmech.ru.feature_app_settings_impl.presentation.screens.lang.SelectLanguageFragment
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.compose.AppSettingsComposeScreen
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsEvent.Ui
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsStoreFactory
import kekmech.ru.feature_app_settings_impl.presentation.screens.map_type.SelectMapTypeFragment
import kekmech.ru.feature_bars_api.BarsLogoutHandle
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.lib_navigation.addScreenForward
import kekmech.ru.lib_navigation.showDialog
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.android.ext.android.inject
import org.koin.compose.koinInject

private const val ACTIVITY_RECREATION_DELAY = 200L

internal class AppSettingsComposeFragment : ComposeFragment() {

    private val logoutHandle by inject<BarsLogoutHandle>()
    private val analytics by screenAnalytics("AppSettings")

    @Composable
    override fun Screen() {
        val factory = koinInject<AppSettingsStoreFactory>()
        val activity = LocalActivity.current as ComponentActivity

        ElmContent(
            key = "AppSettingsScreen",
            storeFactory = { factory.create() },
            viewModelStoreOwner = activity,
            savedStateRegistryOwner = activity,
            content = { onAccept, state, effects ->
                val isDarkTheme = state.appSettings?.isDarkThemeEnabled ?: false
                MpeixTheme(darkTheme = isDarkTheme) {
                    val isRootTab = parentFragment != null
                    AppSettingsComposeScreen(
                        state = state,
                        effects = effects,
                        onAccept = onAccept,
                        onBackClick = if (isRootTab) null else ({ close() }),
                        onOpenFavorites = {
                            analytics.sendClick("Favorites")
                            addScreenForward { FavoritesFragment() }
                        },
                        onOpenGithub = {
                            analytics.sendClick("GitHub")
                            requireContext().openLinkExternal("https://github.com/ghostoftheeleven/mpei-next")
                        },
                        onOpenContributor = { url ->
                            requireContext().openLinkExternal(url)
                        },
                        onLogoutBars = {
                            analytics.sendClick("BarsLogout")
                            if (logoutHandle.logout()) {
                                Toast
                                    .makeText(requireContext(), "Вы вышли из БАРС", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                        onOpenLanguageSelection = { selectedLanguage ->
                            showDialog {
                                SelectLanguageFragment.newInstance(selectedLanguage, LANGUAGE_RESULT_KEY)
                            }
                            setResultListener(
                                key = LANGUAGE_RESULT_KEY,
                                resultMapper = Bundle::notNullString,
                            ) { result ->
                                onAccept(Ui.Action.LanguageChanged(result))
                            }
                        },
                        onOpenMapTypeSelection = { selectedMapType ->
                            showDialog {
                                SelectMapTypeFragment.newInstance(selectedMapType, MAP_TYPE_RESULT_KEY)
                            }
                            setResultListener(
                                key = MAP_TYPE_RESULT_KEY,
                                resultMapper = Bundle::notNullString,
                            ) { result ->
                                onAccept(Ui.Action.MapTypeChanged(result))
                            }
                        },
                        onRecreateActivity = {
                            Handler(Looper.getMainLooper()).postDelayed(
                                { activity.recreate() },
                                ACTIVITY_RECREATION_DELAY,
                            )
                        },
                    )
                }
            },
        )
    }

    companion object {
        private const val LANGUAGE_RESULT_KEY = "LANGUAGE_RESULT_KEY"
        private const val MAP_TYPE_RESULT_KEY = "MAP_TYPE_RESULT_KEY"
    }
}
