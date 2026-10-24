package kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm

import kekmech.ru.feature_bars_impl.domain.AssessedDiscipline
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose.DisciplineUiItem
import kekmech.ru.lib_elm.Resource

// region: STATE

internal data class BarsComposeState(
    val profile: Resource<BarsProfile> = Resource.Loading,
    val isRefreshing: Boolean = false,
    val isLoginRequired: Boolean = false,
    val disciplines: Resource<List<DisciplineUiItem>> = Resource.Loading,
    val selectedTab: BarsTab = BarsTab.DISCIPLINES,
    /** Current week of the semester, used to decide whether a control activity is overdue */
    val currentWeek: Int? = null,
    /** Names of the disciplines expanded on the summary tab */
    val expandedSummary: Set<String> = emptySet(),
)

internal enum class BarsTab {
    DISCIPLINES,
    SUMMARY,
}

internal sealed interface AuthStatus {
    data object LoggedIn : AuthStatus
    data object LoginRequired : AuthStatus
}

// endregion: STATE

internal data class BarsProfile(
    val name: String,
    val group: String,
)

internal sealed interface BarsComposeEvent {
    sealed interface Ui : BarsComposeEvent {
        data object Init : Ui

        sealed interface Click : Ui {
            data object Settings : Click
            data object Login : Click
            data class Discipline(val model: DisciplineUiItem) : Click
            data class Tab(val tab: BarsTab) : Click
            data class SummaryDiscipline(val name: String) : Click
        }

        sealed interface Action : Ui {
            data object PullToRefresh : Action
        }
    }

    sealed interface Internal : BarsComposeEvent {
        data class SubscribeAuthStatusSuccess(val authStatus: AuthStatus) : Internal
        data class CheckAuthStatusFailure(val throwable: Throwable) : Internal
        data class GetProfileSuccess(val profile: BarsProfile) : Internal
        data class GetMarksSuccess(
            val disciplines: List<AssessedDiscipline>,
            val fromCache: Boolean,
        ) : Internal
        data class GetMarksFailure(val throwable: Throwable) : Internal
        data class GetCurrentWeekSuccess(val week: Int?) : Internal
    }
}

internal sealed interface BarsComposeCommand {
    data object SubscribeAuthStatus : BarsComposeCommand
    data object OpenAppSettings : BarsComposeCommand
    data object OpenLogin : BarsComposeCommand
    data object GetProfile : BarsComposeCommand
    data object GetMarks : BarsComposeCommand
    data object GetCurrentWeek : BarsComposeCommand
    data class OpenDetails(val discipline: AssessedDiscipline) : BarsComposeCommand
}

internal sealed interface BarsComposeEffect
