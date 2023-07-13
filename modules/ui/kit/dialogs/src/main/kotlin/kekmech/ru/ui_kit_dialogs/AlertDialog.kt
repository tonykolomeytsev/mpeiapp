package kekmech.ru.ui_kit_dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import kekmech.ru.ui_theme.theme.MpeixTheme

@Stable
class AlertDialogState(dialogVisible: Boolean = false) {

    var dialogVisible by mutableStateOf(dialogVisible)
        private set

    fun showDialog() {
        dialogVisible = true
    }

    fun hideDialog() {
        dialogVisible = false
    }
}

@Composable
fun rememberAlertDialogState(dialogVisible: Boolean = false): AlertDialogState =
    remember { AlertDialogState(dialogVisible) }

/**
 * # AlertDialog (Basic)
 *
 * Mapping implementation to [androidx.compose.material3.AlertDialog].
 * Dialogs provide important prompts in a user flow. They can require an action, communicate
 * information, or help users accomplish a task.
 *
 * By default, the displayed dialog has the minimum height and width that the Material Design spec
 * defines. If required, these constraints can be overwritten by providing a `width` or `height`
 * [Modifier]s.
 *
 * Usage:
 *
 * ```kotlin
 * val myAlert = rememberAlertDialogState()
 *
 * Button(
 *     onClick = { myAlert.showDialog() },
 * ) { ... }
 *
 * AlertDialog(
 *     onDismissRequest = { myAlert.hideDialog() },
 *     state = myAlert,
 * ) { ... }
 * ```
 *
 * @param onDismissRequest called when the user tries to dismiss the Dialog by clicking outside
 * or pressing the back button. This is not called when the dismiss button is clicked.
 * @param state the state by which the showing and hiding of the dialog is controlled. See
 * [AlertDialogState.showDialog] and [AlertDialogState.hideDialog]
 * @param modifier the [Modifier] to be applied to this dialog's content.
 * @param properties typically platform specific properties to further configure the dialog.
 * @param content the content of the dialog
 *
 * @see AlertDialogState
 */
@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit,
    state: AlertDialogState,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    content: @Composable (PaddingValues) -> Unit,
) {
    AnimatedVisibility(
        visible = state.dialogVisible,
        enter = fadeIn(),
        exit = fadeOut(),
        label = "AlertDialogAnimatedVisibility"
    ) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            properties = properties,
        ) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = MpeixTheme.palette.surface,
                contentColor = MpeixTheme.palette.content,
            ) {
                content.invoke(PaddingValues(24.dp))
            }
        }
    }
}

/**
 * # AlertDialog (With layout)
 *
 * Mapping implementation to [androidx.compose.material3.AlertDialog].
 * Dialogs provide important prompts in a user flow. They can require an action, communicate
 * information, or help users accomplish a task.
 *
 * The dialog will position its buttons, typically [TextButton]s, based on the available space.
 * By default it will try to place them horizontally next to each other and fallback to horizontal
 * placement if not enough space is available.
 *
 * Usage:
 *
 * ```kotlin
 * val myAlert = rememberAlertDialogState()
 *
 * Button(
 *     onClick = { myAlert.showDialog() },
 * ) { ... }
 *
 * AlertDialog(
 *     onDismissRequest = { myAlert.hideDialog() },
 *     confirmButton = { ... },
 *     state = myAlert,
 *     ...
 * )
 * ```
 *
 * @param onDismissRequest called when the user tries to dismiss the Dialog by clicking outside
 * or pressing the back button. This is not called when the dismiss button is clicked.
 * @param confirmButton button which is meant to confirm a proposed action, thus resolving what
 * triggered the dialog. The dialog does not set up any events for this button so they need to be
 * set up by the caller.
 * @param state the state by which the showing and hiding of the dialog is controlled. See
 * [AlertDialogState.showDialog] and [AlertDialogState.hideDialog]
 * @param modifier the [Modifier] to be applied to this dialog
 * @param dismissButton button which is meant to dismiss the dialog. The dialog does not set up any
 * events for this button so they need to be set up by the caller.
 * @param icon optional icon that will appear above the [title] or above the [text], in case a
 * title was not provided.
 * @param title title which should specify the purpose of the dialog. The title is not mandatory,
 * because there may be sufficient information inside the [text].
 * @param text text which presents the details regarding the dialog's purpose.
 * @param properties typically platform specific properties to further configure the dialog.
 *
 * @see AlertDialogState
 */
@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit,
    state: AlertDialogState,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    title: String? = null,
    text: String? = null,
    content: @Composable (() -> Unit)? = null,
    properties: DialogProperties = DialogProperties(),
) {
    AnimatedVisibility(
        visible = state.dialogVisible,
        enter = fadeIn(),
        exit = fadeOut(),
        label = "AlertDialogAnimatedVisibility"
    ) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = confirmButton,
            modifier = modifier,
            dismissButton = dismissButton,
            icon = icon,
            title = if (title != null) {
                @Composable {
                    Text(
                        text = title,
                        modifier = Modifier.fillMaxWidth(),
                        style = MpeixTheme.typography.header3,
                        textAlign = TextAlign.Center,
                    )
                }
            } else null,
            text = when {
                text != null && content != null -> {
                    {
                        Column {
                            Text(
                                text = text,
                                style = MpeixTheme.typography.paragraphNormal,
                                color = MpeixTheme.palette.contentVariant,
                                modifier = Modifier.padding(bottom = 16.dp),
                            )
                            Divider()
                            content.invoke()
                            Divider()
                        }
                    }
                }
                text != null && content == null -> {
                    {
                        Text(
                            text = text,
                            style = MpeixTheme.typography.paragraphNormal,
                            color = MpeixTheme.palette.contentVariant,
                        )
                    }
                }
                text == null && content != null -> content
                else -> null
            },
            shape = RoundedCornerShape(28.dp),
            containerColor = MpeixTheme.palette.surface,
            iconContentColor = MpeixTheme.palette.contentVariant,
            titleContentColor = MpeixTheme.palette.content,
            textContentColor = MpeixTheme.palette.contentVariant,
            tonalElevation = 0.dp,
            properties = properties,
        )
    }
}

@Preview
@Composable
private fun BasicAlertDialogPreview() {
    MpeixTheme {
        val alertDialogState = rememberAlertDialogState(dialogVisible = true)
        AlertDialog(
            onDismissRequest = { /* no-op */ },
            state = alertDialogState,
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "Some dialog content1")
                Text(text = "Some dialog content2")
            }
        }
    }
}

@Preview
@Composable
private fun LayoutAlertDialogPreview() {
    MpeixTheme {
        val alertDialogState = rememberAlertDialogState(dialogVisible = true)
        AlertDialog(
            onDismissRequest = { /* no-op */ },
            state = alertDialogState,
            confirmButton = {
                TextButton(
                    onClick = { /* no-op */ },
                ) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { /* no-op */ },
                ) {
                    Text(text = "Dismiss")
                }
            },
            icon = {
                Icon(Icons.Outlined.ShoppingCart, contentDescription = null)
            },
            title = "Are you sure?",
            text = "Are you really sure what you do? Just go out and touch the grass:",
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(MpeixTheme.palette.surfacePlus1),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "Some grass to be touched")
                }
            }
        )
    }
}
