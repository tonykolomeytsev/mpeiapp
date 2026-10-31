package kekmech.ru.ui_kit_buttons.button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import kekmech.ru.ui_theme.theme.MpeixTheme

@Immutable
public sealed class ButtonPalette {

    internal abstract val background: Color @Composable @ReadOnlyComposable get

    internal abstract val text: Color @Composable @ReadOnlyComposable get

    public data object Primary : ButtonPalette() {
        override val background: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.primary
        override val text: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.onPrimary
    }

    public data object Secondary : ButtonPalette() {
        override val background: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.secondary
        override val text: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.onSecondary
    }
}