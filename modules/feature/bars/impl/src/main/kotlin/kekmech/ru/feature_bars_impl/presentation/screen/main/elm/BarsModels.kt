package kekmech.ru.feature_bars_impl.presentation.screen.main.elm

import androidx.compose.runtime.Immutable
import kekmech.ru.feature_bars_impl.domain.AssessedDiscipline
import kekmech.ru.feature_bars_impl.presentation.screen.main.compose.DisciplineUiItem
import kekmech.ru.lib_elm.Resource

// region: STATE

@Immutable
internal data class BarsState(
    val profile: Resource<BarsProfile> = Resource.Loading,
    val isLoginRequired: Boolean = false,
    val disciplines: Resource<List<DisciplineUiItem>> = Resource.Loading,
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

internal sealed interface BarsEvent {
    sealed interface Ui : BarsEvent {
        data object Init : Ui

        sealed interface Click : Ui {
            data object Settings : Click
            data object Login : Click
            data object Profile : Click
            data class Discipline(val model: DisciplineUiItem) : Click
        }

        sealed interface Action : Ui {
            data object PullToRefresh : Action
        }
    }

    sealed interface Internal : BarsEvent {
        data class SubscribeAuthStatusSuccess(val authStatus: AuthStatus) : Internal
        data class CheckAuthStatusFailure(val throwable: Throwable) : Internal
        data class GetProfileSuccess(val profile: BarsProfile) : Internal
        data class GetMarksSuccess(val disciplines: List<AssessedDiscipline>) : Internal
        data class GetMarksFailure(val throwable: Throwable) : Internal
    }
}

internal sealed interface BarsComposeCommand {
    data object SubscribeAuthStatus : BarsComposeCommand
    data object OpenAppSettings : BarsComposeCommand
    data object OpenLogin : BarsComposeCommand
    data object OpenAccounts : BarsComposeCommand
    data object GetProfile : BarsComposeCommand
    data object GetMarks : BarsComposeCommand
    data object Refresh : BarsComposeCommand
    data class OpenDetails(val discipline: AssessedDiscipline) : BarsComposeCommand
}

internal sealed interface BarsEffect
