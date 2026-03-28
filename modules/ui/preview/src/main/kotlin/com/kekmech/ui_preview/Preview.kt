package com.kekmech.ui_preview

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.fragment.app.FragmentManager
import com.kekmech.lib_fragment.LocalRouter
import kekmech.ru.lib_navigation.ActivityCommand
import kekmech.ru.lib_navigation.Command
import kekmech.ru.lib_navigation.Router
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
public fun MpeixPreview(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val routerStub = remember {
        object : Router {
            override fun executeCommand(command: ActivityCommand) = Unit
            override fun executeCommand(vararg commands: Command) = Unit
            override fun executeCommand(fragmentManager: FragmentManager, command: Command) = Unit
        }
    }
    CompositionLocalProvider(
        LocalRouter provides routerStub
    ) {
        MpeixTheme(darkTheme, content)
    }
}