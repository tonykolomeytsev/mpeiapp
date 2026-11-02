package com.kekmech.lib_fragment

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import androidx.fragment.compose.AndroidFragment
import kekmech.ru.lib_navigation.BottomTab
import kekmech.ru.lib_navigation.BottomTabsSwitcher
import kekmech.ru.lib_navigation.features.ScrollToTop
import kotlinx.coroutines.flow.filter
import org.koin.compose.koinInject

/**
 * Экран-таб, который все еще написан на фрагменте с XML разметкой.
 *
 * Повторное нажатие на таб в нижней навигации вызывает [ScrollToTop.onScrollToTop].
 */
@Composable
public inline fun <reified T : Fragment> TabFragment(
    tab: BottomTab,
    modifier: Modifier = Modifier,
) {
    TabFragment(T::class.java, tab, modifier)
}

@Composable
public fun <T : Fragment> TabFragment(
    clazz: Class<T>,
    tab: BottomTab,
    modifier: Modifier = Modifier,
) {
    val bottomTabsSwitcher = koinInject<BottomTabsSwitcher>()
    var fragment by remember { mutableStateOf<Fragment?>(null) }
    AndroidFragment(
        clazz = clazz,
        // AndroidFragment по умолчанию оборачивает контент, и XML экран получает нулевую высоту
        modifier = modifier.fillMaxSize(),
        onUpdate = { fragment = it },
    )
    LaunchedEffect(tab) {
        bottomTabsSwitcher.reselects
            .filter { it == tab }
            .collect { (fragment as? ScrollToTop)?.onScrollToTop() }
    }
}
