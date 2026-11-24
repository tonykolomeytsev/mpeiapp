package kekmech.ru.lib_navigation

import android.view.WindowManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope

private const val BOTTOM_SHEET_METADATA_KEY = "mpeix.bottomSheet"

/**
 * Показывает экраны, зарегистрированные через [bottomSheetEntry], в [ModalBottomSheet]
 * поверх предыдущего экрана. Шторка живет в back stack'е как обычный экран:
 * закрытие шторки свайпом или кнопкой "назад" убирает ее ключ из back stack'а,
 * а когда ключ убирают из back stack'а (например, `PopBackStack`), шторка уезжает вниз.
 */
public class BottomSheetSceneStrategy<T : Any> : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val lastEntry = entries.lastOrNull() ?: return null
        val options = lastEntry.metadata[BOTTOM_SHEET_METADATA_KEY] as? BottomSheetOptions
            ?: return null
        return BottomSheetScene(
            key = lastEntry.contentKey,
            entry = lastEntry,
            previousEntries = entries.dropLast(1),
            overlaidEntries = entries.dropLast(1),
            options = options,
            onBack = onBack,
        )
    }
}

private data class BottomSheetOptions(
    val properties: ModalBottomSheetProperties,
    val skipPartiallyExpanded: Boolean,
    val isBare: Boolean,
)

@OptIn(ExperimentalMaterial3Api::class)
private data class BottomSheetScene<T : Any>(
    override val key: Any,
    private val entry: NavEntry<T>,
    override val previousEntries: List<NavEntry<T>>,
    override val overlaidEntries: List<NavEntry<T>>,
    private val options: BottomSheetOptions,
    private val onBack: () -> Unit,
) : OverlayScene<T> {

    // NavDisplay вызывает content и onRemove у одного и того же экземпляра сцены,
    // но onRemove может прийти раньше первой композиции
    private var sheetState: SheetState? = null

    override val entries: List<NavEntry<T>> = listOf(entry)

    override val content: @Composable () -> Unit = {
        val state = rememberModalBottomSheetState(
            skipPartiallyExpanded = options.skipPartiallyExpanded,
        )
        sheetState = state
        val contentNestedScrollConnection = remember { SheetContentNestedScrollConnection() }
        val sheetContent: @Composable () -> Unit = {
            KeepImeTargetUnderSheet()
            Box(Modifier.nestedScroll(contentNestedScrollConnection)) {
                entry.Content()
            }
        }
        if (options.isBare) {
            ModalBottomSheet(
                onDismissRequest = onBack,
                sheetState = state,
                containerColor = Color.Transparent,
                // Цвет контента обычной шторки: по нему окно шторки подбирает
                // цвет иконок системных панелей под светлую или темную тему
                contentColor = contentColorFor(BottomSheetDefaults.ContainerColor),
                dragHandle = null,
                contentWindowInsets = { WindowInsets(0) },
                properties = options.properties,
            ) {
                sheetContent()
            }
        } else {
            ModalBottomSheet(
                onDismissRequest = onBack,
                sheetState = state,
                properties = options.properties,
            ) {
                sheetContent()
            }
        }
    }

    override suspend fun onRemove() {
        // Шторку убрали из back stack'а не жестом (PopBackStack, ReplaceScreen, NewRoot),
        // поэтому прячем ее с анимацией, а не обрываем. После свайпа шторка уже спрятана
        sheetState?.hide()
    }
}

/**
 * Окно шторки не работает с клавиатурой, как `AlertDialog` без полей ввода
 * ([WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM]): клавиатурой все время управляет окно приложения.
 *
 * Иначе окно шторки, получив фокус, становится целью клавиатуры. Когда шторку закрывают, система
 * удаляет окно-цель и на мгновение отдает клавиатуру системному контроллеру, а тот может ее показать:
 * клавиатура мигает, пока фокус не вернется окну приложения. Шторке с полем ввода флаг нужно будет снять.
 *
 * Вызывается внутри контента шторки: он компонуется, когда окно шторки уже добавлено,
 * но еще не получило фокус.
 */
@Composable
private fun KeepImeTargetUnderSheet() {
    val window = (LocalView.current.parent as? DialogWindowProvider)?.window
    DisposableEffect(window) {
        window?.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        onDispose {}
    }
}

/**
 * Регистрирует экран, который открывается в нижней шторке (см. [BottomSheetSceneStrategy]).
 *
 * @param skipPartiallyExpanded высокая шторка сразу открывается целиком, без промежуточного
 * наполовину открытого состояния
 * @param isBare шторка без своего фона, drag handle и отступов под системные панели:
 * все это рисует сам экран, например через `BottomSheet` из `ui_kit_dialogs`
 */
public inline fun <reified K : ScreenKey> EntryProviderScope<ScreenKey>.bottomSheetEntry(
    properties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
    skipPartiallyExpanded: Boolean = false,
    isBare: Boolean = false,
    noinline content: @Composable (K) -> Unit,
) {
    entry<K>(
        metadata = bottomSheetMetadata(properties, skipPartiallyExpanded, isBare),
        content = content,
    )
}

/** Экран зарегистрирован через [bottomSheetEntry] и открывается в нижней шторке */
public val NavEntry<*>.isBottomSheet: Boolean
    get() = BOTTOM_SHEET_METADATA_KEY in metadata

@PublishedApi
internal fun bottomSheetMetadata(
    properties: ModalBottomSheetProperties,
    skipPartiallyExpanded: Boolean,
    isBare: Boolean,
): Map<String, Any> = mapOf(
    BOTTOM_SHEET_METADATA_KEY to BottomSheetOptions(properties, skipPartiallyExpanded, isBare),
)
