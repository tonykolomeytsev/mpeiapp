package kekmech.ru.feature_bars_impl.presentation.screen.login.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import kotlinx.coroutines.flow.first

/** Focuses the text field when its step opens, so the keyboard stays up between steps. */
@Composable
internal fun rememberAutoFocus(): FocusRequester {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val windowInfo = LocalWindowInfo.current
    LaunchedEffect(focusRequester) {
        // The field is composed in the same frame as the screen is pushed,
        // so the focus request is ignored until the window is focused and the screen is laid out
        snapshotFlow { windowInfo.isWindowFocused }.first { it }
        withFrameNanos { }
        focusRequester.requestFocus()
        keyboardController?.show()
    }
    return focusRequester
}
