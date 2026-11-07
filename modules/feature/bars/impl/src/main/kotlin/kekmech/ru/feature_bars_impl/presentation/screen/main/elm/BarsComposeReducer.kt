package kekmech.ru.feature_bars_impl.presentation.screen.main.elm

import androidx.compose.ui.util.fastFirstOrNull
import kekmech.ru.feature_bars_impl.domain.AssessedDiscipline
import kekmech.ru.feature_bars_impl.domain.FinalGradeType
import kekmech.ru.feature_bars_impl.presentation.screen.main.compose.DisciplineUiItem
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsComposeEvent.Internal
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsComposeEvent.Ui
import kekmech.ru.lib_elm.Resource
import kekmech.ru.lib_elm.toResource
import money.vivid.elmslie.core.store.ScreenReducer
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsComposeCommand as Command
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsComposeEffect as Effect
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsComposeEvent as Event
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsComposeState as State

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
                        // Profile and marks are already subscribed to the current profile, see Ui.Init
                        state {
                            copy(
                                isLoginRequired = false,
                            )
                        }
                    }

                    AuthStatus.LoginRequired -> {
                        state {
                            copy(
                                profile = Resource.Loading,
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
                        isLoginRequired = false,
                    )
                }
            }

            is Internal.GetMarksFailure -> {
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
        }

    override fun Result.ui(event: Ui) =
        when (event) {
            is Ui.Init -> {
                commands {
                    +Command.SubscribeAuthStatus
                    +Command.GetProfile
                    +Command.GetMarks
                }
                state {
                    copy(
                        profile = Resource.Loading,
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

            is Ui.Action.PullToRefresh -> {
                commands { +Command.Refresh }
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