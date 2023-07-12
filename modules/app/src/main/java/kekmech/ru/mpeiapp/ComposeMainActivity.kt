package kekmech.ru.mpeiapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.bumble.appyx.core.integrationpoint.NodeComponentActivity
import kekmech.ru.feature_app_settings_api.domain.model.AppTheme
import kekmech.ru.feature_app_settings_api.domain.usecase.ObserveAppThemeUseCase
import kekmech.ru.feature_main_screen_api.presentation.navigation.MainScreenApi
import kekmech.ru.feature_main_screen_api.presentation.navigation.MainScreenDependencyComponent
import kekmech.ru.feature_onboarding_api.presentation.navigation.OnboardingScreenApi
import kekmech.ru.feature_schedule_api.domain.usecase.HasSelectedScheduleUseCase
import kekmech.ru.lib_app_lifecycle.MainActivityLifecycleObserver
import kekmech.ru.mpeiapp.presentation.navigation.ComposableAppRoot
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject

class ComposeMainActivity : NodeComponentActivity() {

    private val observeAppThemeUseCase: ObserveAppThemeUseCase by inject()
    private val hasSelectedScheduleUseCase: HasSelectedScheduleUseCase by inject()
    private val mainActivityLifecycleObservers: List<MainActivityLifecycleObserver> by lazy {
        getKoin().getAll()
    }
    private val mainScreenApi: MainScreenApi by inject()
    private val onboardingScreenApi: OnboardingScreenApi by inject()
    private val mainScreenDependencyComponent: MainScreenDependencyComponent by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        mainActivityLifecycleObservers.forEach { it.onCreate(this) }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            LocaleContextWrapper.updateResourcesV24(this)
        }

        setContent {
            val appTheme by observeAppThemeUseCase.invoke().collectAsState()
            val darkTheme = when (appTheme) {
                AppTheme.Light -> false
                AppTheme.Dark -> true
                AppTheme.System -> isSystemInDarkTheme()
            }

            MpeixTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MpeixTheme.palette.background,
                ) {
                    ComposableAppRoot(
                        authorizedNavTarget = { mainScreenApi.navTarget() },
                        unauthorizedNavTarget = { onboardingScreenApi.navTarget() },
                        authorized = hasSelectedScheduleUseCase.invoke(),
                        mainScreenDependencyComponent = mainScreenDependencyComponent,
                    )
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleContextWrapper.wrapContext(newBase))
    }
}
