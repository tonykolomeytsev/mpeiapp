package kekmech.ru.feature_bars_impl.presentation.screen.main.elm

import androidx.compose.runtime.Immutable
import io.github.tonykolomeytsev.mpeix.sdk.Discipline
import io.github.tonykolomeytsev.mpeix.sdk.DisciplineId
import kekmech.ru.feature_bars_impl.presentation.screen.main.compose.ShowingFromCacheReason
import kekmech.ru.lib_elm.Resource

// region: STATE

@Immutable
internal data class BarsState(
    val profile: Resource<BarsProfile> = Resource.Loading,
    val isLoginRequired: Boolean = false,
    val showingFromCacheReasons: Set<ShowingFromCacheReason> = emptySet(),
    val disciplines: Resource<List<Discipline>> = Resource.Loading,
    val semesterName: String? = null,
    val isLoading: Boolean = false,
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
            data class Discipline(val discipline: io.github.tonykolomeytsev.mpeix.sdk.Discipline) : Click
        }

        sealed interface Action : Ui {
            data object PullToRefresh : Action
        }
    }

    sealed interface Internal : BarsEvent {
        data class SubscribeAuthStatusSuccess(val authStatus: AuthStatus) : Internal
        data class CheckAuthStatusFailure(val throwable: Throwable) : Internal
        data class ObserveProfileSuccess(val profile: BarsProfile) : Internal
        data class CacheReasonsChanged(val reasons: Set<ShowingFromCacheReason>) : Internal
        data class ObserveMarksSuccess(
            val disciplines: List<Discipline>,
            val semesterName: String,
        ) : Internal
        data class ObserveMarksFailure(val throwable: Throwable) : Internal
        data object RefreshSuccess : Internal
    }
}

internal sealed interface BarsComposeCommand {
    data object SubscribeAuthStatus : BarsComposeCommand
    data object OpenAppSettings : BarsComposeCommand
    data object OpenAccounts : BarsComposeCommand
    data object ObserveProfile : BarsComposeCommand
    data object ObserveMarks : BarsComposeCommand
    data object ObserveCacheReasons : BarsComposeCommand
    data object Refresh : BarsComposeCommand
    data class OpenDetails(val disciplineId: DisciplineId) : BarsComposeCommand
}

internal sealed interface BarsEffect
