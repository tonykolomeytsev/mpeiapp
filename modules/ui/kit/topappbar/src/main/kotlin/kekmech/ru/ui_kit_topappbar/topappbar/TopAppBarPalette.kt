package kekmech.ru.ui_kit_topappbar.topappbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import kekmech.ru.ui_theme.theme.MpeixTheme

public sealed class TopAppBarPalette {

    internal abstract val background: Color @Composable @ReadOnlyComposable get
    internal abstract val title: Color @Composable @ReadOnlyComposable get

    public data object Default : TopAppBarPalette() {
        override val background: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.surface
        override val title: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.onSurface
    }
}