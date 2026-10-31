package kekmech.ru.ui_kit_topappbar.topappbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle
import kekmech.ru.ui_theme.theme.MpeixTheme

public sealed class TopAppBarForm {

    internal abstract val textStyle: TextStyle @Composable @ReadOnlyComposable get

    public data object Small : TopAppBarForm() {
        override val textStyle: TextStyle @Composable @ReadOnlyComposable get() = MpeixTheme.typography.titleLarge
    }
}