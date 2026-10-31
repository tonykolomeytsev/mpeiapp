package kekmech.ru.ui_kit_buttons.button

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme

@Immutable
public sealed class ButtonForm {

    internal abstract val minWidth: Dp @Composable @ReadOnlyComposable get
    internal abstract val minHeight: Dp @Composable @ReadOnlyComposable get

    internal abstract val internalHorizontalPadding: Dp @Composable @ReadOnlyComposable get
    internal abstract val internalVerticalPadding: Dp @Composable @ReadOnlyComposable get
    internal abstract val progressDotSize: Dp @Composable @ReadOnlyComposable get
    internal abstract val progressDotSpacing: Dp @Composable @ReadOnlyComposable get
    internal abstract val shape: Shape @Composable @ReadOnlyComposable get
    internal abstract val textStyle: TextStyle @Composable @ReadOnlyComposable get

    public data object Default : ButtonForm() {
        override val minWidth: Dp @Composable get() = 48.dp
        override val minHeight: Dp @Composable get() = 48.dp
        override val internalHorizontalPadding: Dp @Composable @ReadOnlyComposable get() = 16.dp
        override val internalVerticalPadding: Dp @Composable @ReadOnlyComposable get() = 12.dp
        override val progressDotSize: Dp @Composable @ReadOnlyComposable get() = 8.dp
        override val progressDotSpacing: Dp @Composable @ReadOnlyComposable get() = 16.dp
        override val shape: Shape @Composable @ReadOnlyComposable get() = RoundedCornerShape(8.dp)
        override val textStyle: TextStyle @Composable @ReadOnlyComposable get() = MpeixTheme.typography.labelLarge
    }
}