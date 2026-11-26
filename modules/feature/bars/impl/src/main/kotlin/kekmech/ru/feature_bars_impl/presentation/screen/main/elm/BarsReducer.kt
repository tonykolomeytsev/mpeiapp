package kekmech.ru.feature_bars_impl.presentation.screen.main.elm

import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEvent.Internal
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEvent.Ui
import kekmech.ru.lib_elm.Resource
import kekmech.ru.lib_elm.toResource
import money.vivid.elmslie.core.store.ScreenReducer
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsComposeCommand as Command
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEffect as Effect
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEvent as Event
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsState as State

internal class BarsReducer :
    ScreenReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class
    ) {


    override fun Result.internal(event: Internal) =
        when (event) {
            is Internal.SubscribeAuthStatusSuccess -> {
                when (event.authStatus) {
                    AuthStatus.LoggedIn -> {
                        state { copy(isLoginRequired = false) }
                    }

                    AuthStatus.LoginRequired -> {
                        state {
                            copy(
                                profile = Resource.Loading,
                                isLoginRequired = true,
                                showingFromCacheReasons = emptySet(),
                                disciplines = Resource.Loading,
                                semesterName = null,
                                disciplinesUpdatedAt = null,
                                attendance = Resource.Loading,
                            )
                        }
                    }
                }
            }

            is Internal.CheckAuthStatusFailure -> {
                state { copy(profile = Resource.Error(event.throwable)) }
            }

            is Internal.CacheReasonsChanged -> {
                state { copy(showingFromCacheReasons = event.reasons) }
            }

            is Internal.ObserveProfileSuccess -> {
                state {
                    copy(
                        profile = Resource.Data(event.profile),
                        isLoginRequired = false,
                    )
                }
            }

            is Internal.ObserveMarksSuccess -> {
                state {
                    copy(
                        disciplines = event.disciplines.toResource(),
                        semesterName = event.semesterName,
                        disciplinesUpdatedAt = event.updatedAt,
                        isLoginRequired = false,
                    )
                }
            }

            is Internal.ObserveMarksFailure -> {
                state {
                    copy(
                        disciplines = if (disciplines.isData) {
                            disciplines
                        } else {
                            event.throwable.toResource()
                        },
                    )
                }
            }

            is Internal.ObserveAttendanceSuccess -> {
                state { copy(attendance = event.attendance.toResource()) }
            }

            // The brief is hidden without data. A failure keeps what was shown, unless it was another semester:
            // the brief must not show the old semester next to the grades of a new one
            is Internal.ObserveAttendanceFailure -> {
                state {
                    val shown = attendance.value
                    val isSameSemester = event.semesterId == null || event.semesterId == shown?.semesterId
                    copy(
                        attendance = if (shown != null && isSameSemester) {
                            attendance
                        } else {
                            event.throwable.toResource()
                        },
                    )
                }
            }

            is Internal.RefreshSuccess -> {
                state { copy(isLoading = false) }
            }
        }

    override fun Result.ui(event: Ui) =
        when (event) {
            is Ui.Init -> {
                commands {
                    +Command.SubscribeAuthStatus
                    +Command.ObserveProfile
                    +Command.ObserveMarks
                    +Command.ObserveAttendance
                    +Command.ObserveCacheReasons
                }
                state {
                    copy(
                        profile = Resource.Loading,
                    )
                }
            }

            is Ui.Click.Login -> {
                commands { +Command.OpenAccounts }
            }

            is Ui.Click.Profile -> {
                commands { +Command.OpenAccounts }
            }

            is Ui.Click.Settings -> {
                commands { +Command.OpenAppSettings }
            }

            is Ui.Click.Discipline -> {
                // The details screen reads the discipline from the SDK by its id: stable across refreshes
                commands { +Command.OpenDetails(event.discipline.id) }
            }

            is Ui.Click.Attendance -> {
                // The screen shows the semester of the brief, even if the grades moved on meanwhile
                commands { +state.attendance.value?.let { Command.OpenAttendance(it.semesterId) } }
            }

            is Ui.Action.PullToRefresh -> {
                state { copy(isLoading = true) }
                commands { +Command.Refresh }
            }
        }
}
