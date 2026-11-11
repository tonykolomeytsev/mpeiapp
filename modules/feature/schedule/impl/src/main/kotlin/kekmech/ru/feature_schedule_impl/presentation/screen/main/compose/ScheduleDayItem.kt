package kekmech.ru.feature_schedule_impl.presentation.screen.main.compose

import androidx.compose.runtime.Immutable
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_ui.compose.ClassesUiModel

/**
 * Single page of the [ScheduleDaysPager]: the list of the one working day.
 *
 * @param dayOfWeek day of week, starting from 1 (Monday)
 * @param items items of the day list, created by the `ScheduleClassesListConverter`
 */
@Immutable
internal data class ScheduleDayPage(
    val dayOfWeek: Int,
    val items: List<ScheduleDayItem>,
)

/**
 * Items of the single day list in the [ScheduleDaysPager]
 */
@Immutable
internal sealed interface ScheduleDayItem {

    /**
     * Stable key of the item in the `LazyColumn`
     */
    val key: String

    data class ClassesEntry(
        val index: Int,
        val classes: Classes,
        val model: ClassesUiModel,
    ) : ScheduleDayItem {
        // Classes with the same name may follow each other, so the index is used
        override val key: String get() = "classes_$index"
    }

    data class Window(val index: Int) : ScheduleDayItem {
        override val key: String get() = "window_$index"
    }

    data object Lunch : ScheduleDayItem {
        override val key: String get() = "lunch"
    }

    data object SelfStudy : ScheduleDayItem {
        override val key: String get() = "self_study"
    }

    data object Shimmer : ScheduleDayItem {
        override val key: String get() = "shimmer"
    }

    data class Error(val throwable: Throwable) : ScheduleDayItem {
        override val key: String get() = "error"
    }
}
