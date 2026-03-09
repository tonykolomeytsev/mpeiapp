package com.kekmech.lib_fragment

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import kekmech.ru.lib_navigation.Router

public val LocalRouter: ProvidableCompositionLocal<Router> = staticCompositionLocalOf {
    error("No LocalRouter provided!")
}
