package kekmech.ru.ui_kit_switch.embedded

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
public sealed class EmbeddedSwitchForm {

    internal abstract val width: Dp
    internal abstract val height: Dp

    public data object S : EmbeddedSwitchForm() {

        override val width: Dp = 48.dp
        override val height: Dp = 28.dp
    }

    public data object M : EmbeddedSwitchForm() {

        override val width: Dp = 52.dp
        override val height: Dp = 32.dp
    }
}
