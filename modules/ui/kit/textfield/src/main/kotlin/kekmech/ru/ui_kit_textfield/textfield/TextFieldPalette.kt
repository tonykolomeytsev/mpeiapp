package kekmech.ru.ui_kit_textfield.textfield

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import kekmech.ru.ui_theme.theme.MpeixTheme

@Immutable
public sealed class TextFieldPalette {

    internal abstract val container: Color @Composable @ReadOnlyComposable get
    internal abstract val disabledContainer: Color @Composable @ReadOnlyComposable get

    internal abstract val text: Color @Composable @ReadOnlyComposable get
    internal abstract val disabledContent: Color @Composable @ReadOnlyComposable get

    /** Cursor, focused indicator, focused label and text selection */
    internal abstract val accent: Color @Composable @ReadOnlyComposable get

    /** Unfocused indicator */
    internal abstract val indicator: Color @Composable @ReadOnlyComposable get

    /** Unfocused label, placeholder, supporting text and icons */
    internal abstract val secondaryContent: Color @Composable @ReadOnlyComposable get

    internal abstract val error: Color @Composable @ReadOnlyComposable get

    public data object Default : TextFieldPalette() {
        override val container: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.surfaceContainerHighest
        override val disabledContainer: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.onSurface.copy(alpha = 0.04f)
        override val text: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.onSurface
        override val disabledContent: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.onSurface.copy(alpha = 0.38f)
        override val accent: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.primary
        override val indicator: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.onSurfaceVariant
        override val secondaryContent: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.onSurfaceVariant
        override val error: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.error
    }
}
