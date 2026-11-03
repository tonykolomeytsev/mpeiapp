package kekmech.ru.ui_kit_navigationbar.floating

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import kekmech.ru.ui_theme.theme.MpeixTheme

@Immutable
public sealed class FloatingNavBarPalette {

    internal abstract val background: Color @Composable @ReadOnlyComposable get

    internal abstract val selectedTabBackground: Color @Composable @ReadOnlyComposable get

    internal abstract val selectedContent: Color @Composable @ReadOnlyComposable get

    internal abstract val unselectedContent: Color @Composable @ReadOnlyComposable get

    public data object Default : FloatingNavBarPalette() {
        override val background: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.floatingContainer
        override val selectedTabBackground: Color
            @Composable @ReadOnlyComposable get() = MpeixTheme.palette.floatingContainerHigh
        override val selectedContent: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.primary
        override val unselectedContent: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.onSurface
    }
}
