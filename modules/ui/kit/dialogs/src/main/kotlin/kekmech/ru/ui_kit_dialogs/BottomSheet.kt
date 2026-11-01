package kekmech.ru.ui_kit_dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme

/**
 * Фон шторки: скругленные верхние углы, drag handle и отступ под navigation bar.
 * Используется как корневой элемент экрана внутри `ComposeBottomSheetDialogFragment`.
 *
 * ### Пример использования
 * ```kotlin
 * override fun Screen() {
 *     MpeixTheme {
 *         BottomSheet {
 *             Text("Hello")
 *         }
 *     }
 * }
 * ```
 */
@Composable
public fun BottomSheet(
    modifier: Modifier = Modifier,
    showDragHandle: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = BottomSheetDefaults.ExpandedShape,
        color = MpeixTheme.palette.surface,
        contentColor = MpeixTheme.palette.onSurface,
    ) {
        Column(modifier = Modifier.navigationBarsPadding()) {
            if (showDragHandle) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    BottomSheetDefaults.DragHandle(color = MpeixTheme.palette.outline)
                }
            }
            content()
        }
    }
}

@Composable
@PreviewLightDark
private fun BottomSheetPreview() {
    MpeixTheme {
        BottomSheet {
            Text(
                text = "Bottom sheet content",
                modifier = Modifier.padding(16.dp),
                style = MpeixTheme.typography.bodyLarge,
            )
        }
    }
}
