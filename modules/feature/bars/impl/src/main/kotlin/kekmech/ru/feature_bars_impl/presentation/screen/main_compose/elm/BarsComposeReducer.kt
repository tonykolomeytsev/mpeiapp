package kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm

import androidx.compose.ui.util.fastFirstOrNull
import kekmech.ru.feature_bars_impl.domain.AssessedDiscipline
import kekmech.ru.feature_bars_impl.domain.FinalGradeType
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.compose.DisciplineUiItem
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEvent.Internal
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEvent.Ui
import kekmech.ru.lib_elm.Resource
import kekmech.ru.lib_elm.toResource
import money.vivid.elmslie.core.store.ScreenReducer
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeCommand as Command
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEffect as Effect
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEvent as Event
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeState as State

internal class BarsComposeReducer :
    ScreenReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class
    ) {


    override fun Result.internal(event: Internal) =
        when (event) {
            is Internal.SubscribeAuthStatusSuccess -> {
                when (event.authStatus) {
                    AuthStatus.LoggedIn -> {
                        state {
                            copy(
                                isLoginRequired = false,
                            )
                        }
                        commands {
                            +Command.GetProfile
                                .takeIf { !state.profile.isData }
                            +Command.GetMarks
                            +Command.GetCurrentWeek
                        }
                    }

                    AuthStatus.LoginRequired -> {
                        state {
                            copy(
                                profile = Resource.Loading,
                                isRefreshing = false,
                                isLoginRequired = true,
                                disciplines = Resource.Loading,
                            )
                        }
                    }
                }
            }

            is Internal.CheckAuthStatusFailure -> {
                state {
                    copy(
                        profile = Resource.Error(event.throwable),
                        isRefreshing = false,
                    )
                }
            }

            is Internal.GetProfileSuccess -> {
                state {
                    copy(
                        profile = Resource.Data(event.profile),
                        isLoginRequired = false,
                    )
                }
            }

            is Internal.GetMarksSuccess -> {
                state {
                    copy(
                        disciplines = event.disciplines.map { it.toUi() }.toResource(),
                        isRefreshing = false,
                        isLoginRequired = false,
                    )
                }
            }

            is Internal.GetCurrentWeekSuccess -> {
                state { copy(currentWeek = event.week) }
            }

            is Internal.GetMarksFailure -> {
                state {
                    copy(
                        disciplines = if (disciplines.isData) {
                            disciplines
                        } else {
                            event.throwable.toResource()
                        },
                        isRefreshing = false,
                    )
                }
            }
        }

    override fun Result.ui(event: Ui) =
        when (event) {
            is Ui.Init -> {
                commands { +Command.SubscribeAuthStatus }
                state {
                    copy(
                        profile = Resource.Loading,
                        isRefreshing = true,
                    )
                }
            }

            is Ui.Click.Login -> {
                commands { +Command.OpenLogin }
            }

            is Ui.Click.Settings -> {
                commands { +Command.OpenAppSettings }
            }

            is Ui.Click.Discipline -> {
                commands { +Command.OpenDetails(event.model.discipline) }
            }

            is Ui.Click.Tab -> {
                state { copy(selectedTab = event.tab) }
            }

            is Ui.Click.SummaryDiscipline -> {
                state {
                    copy(
                        expandedSummary = if (event.name in expandedSummary) {
                            expandedSummary - event.name
                        } else {
                            expandedSummary + event.name
                        }
                    )
                }
            }

            is Ui.Action.PullToRefresh -> {
                state {
                    copy(
                        isRefreshing = true,
                        // let the summary show the skeleton again instead of a stale error
                        disciplines = if (disciplines.isError) Resource.Loading else disciplines,
                    )
                }
                commands {
                    +Command.GetProfile
                        .takeIf { !state.profile.isData }
                    +Command.GetMarks
                    +Command.GetCurrentWeek
                }
            }
        }

    private fun AssessedDiscipline.toUi() =
        DisciplineUiItem(
            name = name,
            person = person,
            type = assessmentType,
            finalMark = finalGrades.fastFirstOrNull { it.type == FinalGradeType.FINAL_MARK }?.finalMark
                ?.takeIf { it != -1f },
            marks = controlActivities.map { it.finalMark }.filter { it != -1f },
            discipline = this,
        )
}