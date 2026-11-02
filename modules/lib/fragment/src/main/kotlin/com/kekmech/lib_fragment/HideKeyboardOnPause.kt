package com.kekmech.lib_fragment

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect

/**
 * Прячет клавиатуру, когда экран уходит с переднего плана (как `hideKeyboard()` в `onPause`).
 */
@Composable
public fun HideKeyboardOnPause() {
    val keyboardController = LocalSoftwareKeyboardController.current
    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
        keyboardController?.hide()
    }
}
