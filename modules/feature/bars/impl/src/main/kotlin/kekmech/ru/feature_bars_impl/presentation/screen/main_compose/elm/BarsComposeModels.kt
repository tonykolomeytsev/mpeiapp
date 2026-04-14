package kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm

import kekmech.ru.feature_bars_impl.domain.AssessedDiscipline
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose.DisciplineItem
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose.DisciplineUiItem
import kekmech.ru.lib_elm.Resource

// region: STATE

internal data class BarsComposeState(
    val profile: Resource<BarsProfile> = Resource.Loading,
    val isRefreshing: Boolean = false,
    val isLoginRequired: Boolean = false,
    val disciplines: Resource<List<DisciplineItem>> = Resource.Loading,
)

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
    }
}

internal sealed interface BarsComposeCommand {
    data object SubscribeAuthStatus : BarsComposeCommand
    data object OpenAppSettings : BarsComposeCommand
    data object OpenLogin : BarsComposeCommand
    data object GetProfile : BarsComposeCommand
    data object GetMarks : BarsComposeCommand
    data class OpenDetails(val discipline: AssessedDiscipline) : BarsComposeCommand
}

internal sealed interface BarsComposeEffect
