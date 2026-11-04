package kekmech.ru.feature_dashboard_impl.presentation.screen.main

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule
import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_ui.compose.ClassesUiModel

/**
 * Items of the [DashboardScreen] list, created by the [DashboardListConverter]
 */
@Immutable
internal sealed interface DashboardListItem {

    /**
     * Stable key of the item in the `LazyColumn`
     */
    val key: String

    data class Space(override val key: String, val height: Dp) : DashboardListItem

    data class DayStatus(val header: String, val description: String?) : DashboardListItem {
        override val key: String get() = "day_status"
    }

    data object SearchField : DashboardListItem {
        override val key: String get() = "search_field"
    }

    data class SelectedSchedule(val name: String, val type: ScheduleType) : DashboardListItem {
        override val key: String get() = "selected_schedule"
    }

    data object LunchBanner : DashboardListItem {
        override val key: String get() = "lunch_banner"
    }

    data class SectionHeader(
        val section: Section,
        val title: String,
        val subtitle: String? = null,
        val actionText: String? = null,
    ) : DashboardListItem {
        override val key: String get() = "header_$section"
    }

    data class EmptyState(
        val section: Section,
        val title: String,
        val subtitle: String,
    ) : DashboardListItem {
        override val key: String get() = "empty_$section"
    }

    data object EventsShimmer : DashboardListItem {
        override val key: String get() = "events_shimmer"
    }

    data class EventsError(val throwable: Throwable) : DashboardListItem {
        override val key: String get() = "events_error"
    }

    data class TimePrediction(val text: String) : DashboardListItem {
        override val key: String get() = "time_prediction"
    }

    data class ClassesEntry(
        val index: Int,
        val classes: Classes,
        val model: ClassesUiModel,
    ) : DashboardListItem {
        // Classes with the same name may follow each other, so the index is used
        override val key: String get() = "classes_$index"
    }

    data class NoteEntry(val index: Int, val note: Note) : DashboardListItem {
        override val key: String get() = "note_${note.id ?: "i$index"}"
    }

    data class FavoriteEntry(
        val favoriteSchedule: FavoriteSchedule,
        val isSelected: Boolean,
    ) : DashboardListItem {
        override val key: String get() = "favorite_${favoriteSchedule.name}"
    }

    enum class Section { EVENTS, NOTES, FAVORITES }
}
