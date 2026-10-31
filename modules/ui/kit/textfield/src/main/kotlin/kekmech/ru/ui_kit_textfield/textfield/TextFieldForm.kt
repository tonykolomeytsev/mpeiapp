package kekmech.ru.ui_kit_textfield.textfield

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.ui_theme.theme.MpeixTheme

@Immutable
public sealed class TextFieldForm {

    internal abstract val shape: Shape @Composable @ReadOnlyComposable get
    internal abstract val textStyle: TextStyle @Composable @ReadOnlyComposable get
    internal abstract val supportingTextStyle: TextStyle @Composable @ReadOnlyComposable get

    /**
     * Fixed label line height, which prevents the field from bouncing during label animation.
     * Material counts the animated label height in the field height, so it must stay constant.
     * Material uses 16dp vertical padding unfocused and 8dp focused, so keep it at 16sp,
     * otherwise focused and unfocused field heights will differ.
     */
    internal abstract val labelLineHeight: TextUnit @Composable @ReadOnlyComposable get

    public data object Filled : TextFieldForm() {
        override val shape: Shape @Composable @ReadOnlyComposable get() = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
        override val textStyle: TextStyle @Composable @ReadOnlyComposable get() = MpeixTheme.typography.bodyLarge
        override val supportingTextStyle: TextStyle @Composable @ReadOnlyComposable get() = MpeixTheme.typography.bodySmall
        override val labelLineHeight: TextUnit @Composable @ReadOnlyComposable get() = 16.sp
    }
}
