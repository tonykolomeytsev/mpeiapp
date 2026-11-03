package kekmech.ru.ui_kit_navigationbar.floating

import androidx.compose.foundation.layout.PaddingValues
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
public sealed class FloatingNavBarForm {

    internal abstract val maxWidth: Dp @Composable @ReadOnlyComposable get
    internal abstract val shape: Shape @Composable @ReadOnlyComposable get
    internal abstract val tabShape: Shape @Composable @ReadOnlyComposable get
    internal abstract val elevation: Dp @Composable @ReadOnlyComposable get
    internal abstract val contentPadding: PaddingValues @Composable @ReadOnlyComposable get
    internal abstract val tabContentPadding: PaddingValues @Composable @ReadOnlyComposable get
    internal abstract val tabSpacing: Dp @Composable @ReadOnlyComposable get
    internal abstract val iconSize: Dp @Composable @ReadOnlyComposable get
    internal abstract val labelTextStyle: TextStyle @Composable @ReadOnlyComposable get

    public data object Default : FloatingNavBarForm() {
        override val maxWidth: Dp @Composable @ReadOnlyComposable get() = 480.dp
        override val shape: Shape @Composable @ReadOnlyComposable get() = RoundedCornerShape(percent = 50)
        override val tabShape: Shape @Composable @ReadOnlyComposable get() = RoundedCornerShape(percent = 50)
        override val elevation: Dp @Composable @ReadOnlyComposable get() = 12.dp
        override val contentPadding: PaddingValues
            @Composable @ReadOnlyComposable get() = PaddingValues(all = 4.dp)
        override val tabContentPadding: PaddingValues
            @Composable @ReadOnlyComposable get() = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
        override val tabSpacing: Dp @Composable @ReadOnlyComposable get() = 0.dp
        override val iconSize: Dp @Composable @ReadOnlyComposable get() = 24.dp
        override val labelTextStyle: TextStyle @Composable @ReadOnlyComposable get() = MpeixTheme.typography.labelSmall
    }
}
