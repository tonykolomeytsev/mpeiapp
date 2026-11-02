package com.kekmech.lib_fragment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.fragment.app.Fragment
import kekmech.ru.lib_navigation.di.RouterHolder

/**
 * Зависимости, нужные внутри экранов для взаимодействия с внешним миром
 */
@Composable
internal fun Fragment.ProvideScreenDependencies(
    resultController: ResultController,
    content: @Composable () -> Unit,
) {
    val router = (requireContext().applicationContext as RouterHolder).router
    CompositionLocalProvider(
        LocalRouter provides router,
        LocalResultController provides resultController,
        content = content,
    )
}
