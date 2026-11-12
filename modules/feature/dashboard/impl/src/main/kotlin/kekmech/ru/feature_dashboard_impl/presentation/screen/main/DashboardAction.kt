package kekmech.ru.feature_dashboard_impl.presentation.screen.main

import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule
import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_schedule_api.domain.model.Classes

/**
 * User actions on the [DashboardScreen], handled by the [DashboardEntry]
 */
internal sealed interface DashboardAction {
    data object Refresh : DashboardAction
    data object Reload : DashboardAction
    data object SearchClick : DashboardAction
    data object ChangeScheduleClick : DashboardAction
    data object LunchBannerClick : DashboardAction
    data object WhatsNewBannerClick : DashboardAction
    data object WhatsNewBannerDismissClick : DashboardAction
    data object ShowAllNotesClick : DashboardAction
    data object SetUpFavoritesClick : DashboardAction
    data object ListScrolled : DashboardAction
    data class ClassesClick(val classes: Classes) : DashboardAction
    data class NoteClick(val note: Note) : DashboardAction
    data class FavoriteScheduleClick(val favoriteSchedule: FavoriteSchedule) : DashboardAction
}
