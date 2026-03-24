package com.kekmech.feature_bars_auth_impl.presentation.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.kekmech.feature_bars_auth_impl.presentation.screens.main.elm.BarsAuthMainStoreFactory
import com.kekmech.lib_fragment.ComposeFragment
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.compose.koinInject

internal class BarsAuthMainFragment : ComposeFragment() {

    @Composable
    override fun Screen() {
        val appSettings = koinInject<AppSettingsRepository>()
        val darkTheme =
            remember { derivedStateOf { appSettings.getAppSettings().isDarkThemeEnabled } }
        MpeixTheme(darkTheme = darkTheme.value) {
            val factory = koinInject<BarsAuthMainStoreFactory>()
            ElmContent(
                key = "BarsAuthMainScreen",
                storeFactory = { factory.create() },
                content = ::BarsAuthMainScreen,
            )
        }
    }
}