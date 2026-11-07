package kekmech.ru.feature_bars_impl.presentation.screen.login.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_kit_buttons.button.Button
import kekmech.ru.ui_kit_buttons.button.ButtonPalette
import kekmech.ru.ui_kit_buttons.button.ButtonState
import kekmech.ru.ui_kit_topappbar.TopAppBarNavigationIconScope
import kekmech.ru.ui_theme.theme.MpeixTheme

internal val FieldShape = RoundedCornerShape(8.dp)

/** Header with an optional back button, then the step content. */
@Composable
internal fun SheetLayout(
    title: String,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    isWorking: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
    ) {
        Box(Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(start = if (onBack != null) 4.dp else 16.dp, end = 16.dp),
            ) {
                if (onBack != null) {
                    TopAppBarNavigationIconScope.BackIconButton(onClick = onBack)
                }
                Column {
                    Text(
                        text = title,
                        style = MpeixTheme.typography.titleLarge,
                        color = MpeixTheme.palette.onSurface,
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MpeixTheme.typography.bodyMedium,
                            color = MpeixTheme.palette.onSurfaceVariant,
                        )
                    }
                }
            }
            // Lists have no button to show the progress, so it is shown under the header
            if (isWorking) {
                LinearProgressIndicator(
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                )
            }
        }
        content()
    }
}

/** Big icon, title and description in the center, for the terminal steps. */
@Composable
internal fun StatusLayout(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    description: String,
    bottom: @Composable () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 16.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(72.dp),
        )
        Text(
            text = title,
            style = MpeixTheme.typography.titleLarge,
            color = MpeixTheme.palette.onSurface,
        )
        Text(
            text = description,
            style = MpeixTheme.typography.bodyLarge,
            color = MpeixTheme.palette.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        bottom()
    }
}

/** Focuses the text field when its step opens, so the keyboard stays up between steps. */
@Composable
internal fun rememberAutoFocus(): FocusRequester {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(focusRequester) { focusRequester.requestFocus() }
    return focusRequester
}

@Composable
internal fun Description(text: String) {
    Text(
        text = text,
        style = MpeixTheme.typography.bodyLarge,
        color = MpeixTheme.palette.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
}

@Composable
internal fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    isWorking: Boolean = false,
    palette: ButtonPalette = ButtonPalette.Primary,
) {
    Button(
        text = text,
        onClick = onClick,
        palette = palette,
        state = if (isWorking) ButtonState.Loading else ButtonState.Default,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 4.dp),
    )
}
