package com.kekmech.feature_bars_auth_impl.presentation.screens.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kekmech.lib_fragment.LocalRouter
import com.kekmech.ui_preview.MpeixPreview
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
internal fun LoadingScreen() {
    val router = LocalRouter.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = "Вход в БАРС МЭИ",
                navigationIcon = {
                    BackIconButton {
                        router.executeCommand(PopBackStack())
                    }
                },
            )
        },
        containerColor = MpeixTheme.palette.background,
        contentColor = MpeixTheme.palette.content,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(56.dp),
                color = MpeixTheme.palette.primary,
            )
        }
    }
}

@Preview
@Composable
private fun LoadingScreenPreview() {
    MpeixPreview {
        LoadingScreen()
    }
}