package kekmech.ru.feature_schedule_ui.compose

import androidx.compose.runtime.Immutable
import kekmech.ru.feature_schedule_api.domain.model.Classes
import java.time.LocalDate

/**
 * Single page of the [ScheduleDaysPager]: the list of the one working day.
 *
 * @param dayOfWeek day of week, starting from 1 (Monday)
 * @param items items of the day list, see [ScheduleDayPagesConverter]
 */
@Immutable
public data class ScheduleDayPage(
    val dayOfWeek: Int,
    val items: List<ScheduleDayItem>,
)

/**
 * Items of the single day list in the [ScheduleDaysPager]
 */
@Immutable
public sealed interface ScheduleDayItem {

    /**
     * Stable key of the item in the `LazyColumn`
     */
    public val key: String

    public data class ClassesEntry(
        val index: Int,
        val classes: Classes,
        val model: ParallelClassesUi,
    ) : ScheduleDayItem {
        // Classes with the same name may follow each other, so the index is used
        override val key: String get() = "classes_$index"
    }

    public data class Window(val index: Int) : ScheduleDayItem {
        override val key: String get() = "window_$index"
    }

    public data object Lunch : ScheduleDayItem {
        override val key: String get() = "lunch"
    }

    /**
     * @param date date of the day, the seed of the [SelfStudyItem] illustration
     */
    public data class SelfStudy(val date: LocalDate) : ScheduleDayItem {
        override val key: String get() = "self_study"
    }

    public data object Shimmer : ScheduleDayItem {
        override val key: String get() = "shimmer"
    }

    public data class Error(val throwable: Throwable) : ScheduleDayItem {
        override val key: String get() = "error"
    }
}
