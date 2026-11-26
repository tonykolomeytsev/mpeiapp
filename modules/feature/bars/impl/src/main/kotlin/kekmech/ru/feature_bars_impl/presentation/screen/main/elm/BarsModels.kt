package kekmech.ru.feature_bars_impl.presentation.screen.main.elm

import androidx.compose.runtime.Immutable
import io.github.tonykolomeytsev.mpeix.sdk.Attendance
import io.github.tonykolomeytsev.mpeix.sdk.Discipline
import io.github.tonykolomeytsev.mpeix.sdk.DisciplineId
import io.github.tonykolomeytsev.mpeix.sdk.SemesterId
import kekmech.ru.feature_bars_impl.presentation.compose.olderOf
import kekmech.ru.feature_bars_impl.presentation.screen.main.compose.ShowingFromCacheReason
import kekmech.ru.lib_elm.Resource
import kotlin.time.Instant

// region: STATE

@Immutable
internal data class BarsState(
    val profile: Resource<BarsProfile> = Resource.Loading,
    val isLoginRequired: Boolean = false,
    val showingFromCacheReasons: Set<ShowingFromCacheReason> = emptySet(),
    val disciplines: Resource<List<Discipline>> = Resource.Loading,
    val semesterName: String? = null,
    /** When the grades were downloaded, null until they are loaded. */
    val disciplinesUpdatedAt: Instant? = null,
    val attendance: Resource<SemesterAttendance> = Resource.Loading,
    val isLoading: Boolean = false,
) {
    /** The older of the grades and the attendance brief: everything on screen is at least this fresh. */
    val updatedAt: Instant?
        get() = olderOf(disciplinesUpdatedAt, attendance.value?.updatedAt)
}

/** The "Всего" row of the semester whose grades are on screen. [semesterId] opens the attendance screen. */
internal data class SemesterAttendance(
    val semesterId: SemesterId,
    val total: Attendance,
    /** When the attendance page was downloaded. */
    val updatedAt: Instant? = null,
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
            data object Attendance : Click
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
            val updatedAt: Instant?,
        ) : Internal
        data class ObserveMarksFailure(val throwable: Throwable) : Internal
        data class ObserveAttendanceSuccess(val attendance: SemesterAttendance) : Internal
        /** [semesterId] is null when the failure is not about one semester. */
        data class ObserveAttendanceFailure(val throwable: Throwable, val semesterId: SemesterId? = null) : Internal
        data object RefreshSuccess : Internal
    }
}

internal sealed interface BarsComposeCommand {
    data object SubscribeAuthStatus : BarsComposeCommand
    data object OpenAppSettings : BarsComposeCommand
    data object OpenAccounts : BarsComposeCommand
    data object ObserveProfile : BarsComposeCommand
    data object ObserveMarks : BarsComposeCommand
    data object ObserveAttendance : BarsComposeCommand
    data object ObserveCacheReasons : BarsComposeCommand
    data object Refresh : BarsComposeCommand
    data class OpenDetails(val disciplineId: DisciplineId) : BarsComposeCommand
    data class OpenAttendance(val semesterId: SemesterId) : BarsComposeCommand
}

internal sealed interface BarsEffect
