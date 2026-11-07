package kekmech.ru.lib_navigation

import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
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
 * закрытие шторки свайпом или кнопкой "назад" убирает ее ключ из back stack'а.
 */
public class BottomSheetSceneStrategy<T : Any> : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val lastEntry = entries.lastOrNull() ?: return null
        val properties = lastEntry.metadata[BOTTOM_SHEET_METADATA_KEY] as? ModalBottomSheetProperties
            ?: return null
        return BottomSheetScene(
            key = lastEntry.contentKey,
            entry = lastEntry,
            previousEntries = entries.dropLast(1),
            overlaidEntries = entries.dropLast(1),
            properties = properties,
            onBack = onBack,
        )
    }
}

private data class BottomSheetScene<T : Any>(
    override val key: Any,
    private val entry: NavEntry<T>,
    override val previousEntries: List<NavEntry<T>>,
    override val overlaidEntries: List<NavEntry<T>>,
    private val properties: ModalBottomSheetProperties,
    private val onBack: () -> Unit,
) : OverlayScene<T> {

    override val entries: List<NavEntry<T>> = listOf(entry)

    override val content: @Composable () -> Unit = {
        ModalBottomSheet(
            onDismissRequest = onBack,
            properties = properties,
        ) {
            entry.Content()
        }
    }
}

/**
 * Регистрирует экран, который открывается в нижней шторке (см. [BottomSheetSceneStrategy]).
 */
public inline fun <reified K : ScreenKey> EntryProviderScope<ScreenKey>.bottomSheetEntry(
    properties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
    noinline content: @Composable (K) -> Unit,
) {
    entry<K>(metadata = bottomSheetMetadata(properties), content = content)
}

@PublishedApi
internal fun bottomSheetMetadata(properties: ModalBottomSheetProperties): Map<String, Any> =
    mapOf(BOTTOM_SHEET_METADATA_KEY to properties)
