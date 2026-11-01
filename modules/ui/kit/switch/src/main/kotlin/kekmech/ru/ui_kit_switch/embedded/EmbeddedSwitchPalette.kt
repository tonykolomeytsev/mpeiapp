package kekmech.ru.ui_kit_switch.embedded

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import kekmech.ru.ui_theme.theme.MpeixTheme

@Immutable
public sealed class EmbeddedSwitchPalette {

    internal abstract val checked: Color @Composable @ReadOnlyComposable get
    internal abstract val unchecked: Color @Composable @ReadOnlyComposable get
    internal abstract val disabled: Color @Composable @ReadOnlyComposable get
    internal abstract val disabledChecked: Color @Composable @ReadOnlyComposable get
    internal abstract val borderChecked: Color @Composable @ReadOnlyComposable get
    internal abstract val borderUnchecked: Color @Composable @ReadOnlyComposable get
    internal abstract val icon: Color @Composable @ReadOnlyComposable get

    public data object Default : EmbeddedSwitchPalette() {

        override val checked: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.primary
        override val icon: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.onPrimary
        override val unchecked: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.outline
        override val disabled: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.outlineVariant.copy(alpha = 0.55f)
        override val disabledChecked: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.outlineVariant.copy(alpha = 0.55f)
        override val borderChecked: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.outlineVariant
        override val borderUnchecked: Color @Composable @ReadOnlyComposable get() = MpeixTheme.palette.outlineVariant
    }
}