package kekmech.ru.feature_schedule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.reactivex.exceptions.CompositeException
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.feature_schedule.find_schedule.elm.FindScheduleAction
import kekmech.ru.feature_schedule.find_schedule.elm.FindScheduleEffect
import kekmech.ru.feature_schedule.find_schedule.elm.FindScheduleEvent.News
import kekmech.ru.feature_schedule.find_schedule.elm.FindScheduleEvent.Wish
import kekmech.ru.feature_schedule.find_schedule.elm.FindScheduleReducer
import kekmech.ru.feature_schedule.find_schedule.elm.FindScheduleState
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

class FindScheduleReducerTest : BehaviorSpec({
    val reducer = FindScheduleReducer()
    Given("Initial state") {
        val givenState = STATE
        When("Wish.Init") {
            val (state, effects, actions) = reducer.reduce(Wish.Init, givenState)
            Then("Check state") {
                state shouldBe givenState
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("Wish.Click.Continue") {
            val (state, effects, actions) = reducer.reduce(Wish.Click.Continue(CORRECT_NAME), givenState)
            Then("Check state") {
                state.isLoading.shouldBeTrue()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldContainExactly(FindScheduleAction.FindGroup(CORRECT_NAME))
            }
        }
        When("Wish.Action.GroupNumberChanged (correct)") {
            val (state, effects, actions) = reducer.reduce(Wish.Action.GroupNumberChanged(CORRECT_NAME), givenState)
            Then("Check state") {
                state.isContinueButtonEnabled.shouldBeTrue()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldContainExactly(FindScheduleAction.SearchForAutocomplete(CORRECT_NAME))
            }
        }
        When("Wish.Action.GroupNumberChanged (incorrect)") {
            val (state, effects, actions) = reducer.reduce(Wish.Action.GroupNumberChanged(INVALID_NAME), givenState)
            Then("Check state") {
                state.isContinueButtonEnabled.shouldBeFalse()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldContainExactly(FindScheduleAction.SearchForAutocomplete(INVALID_NAME))
            }
        }
    }
    Given("Loading state") {
        val givenState = STATE.copy(isLoading = true)
        When("News.GroupLoadingError (group not found)") {
            val (state, effects, actions) = reducer.reduce(News.GroupLoadingError(VALIDATION_ERROR), givenState)
            Then("Check state") {
                state.isLoading.shouldBeFalse()
            }
            Then("Check effects") {
                effects.shouldContainExactly(FindScheduleEffect.ShowError)
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("News.GroupLoadingError (unknown error)") {
            val (state, effects, actions) = reducer.reduce(News.GroupLoadingError(UNKNOWN_ERROR), givenState)
            Then("Check state") {
                state.isLoading.shouldBeFalse()
            }
            Then("Check effects") {
                effects.shouldContainExactly(FindScheduleEffect.ShowSomethingWentWrongError)
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("News.News.GroupLoadedSuccessfully") {
            val (state, effects, actions) = reducer.reduce(News.GroupLoadedSuccessfully(CORRECT_NAME), givenState)
            Then("Check state") {
                state.isLoading.shouldBeFalse()
            }
            Then("Check effects") {
                effects.shouldContainExactly(
                    FindScheduleEffect.NavigateNextFragment(givenState.continueTo, CORRECT_NAME)
                )
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
    }
    Given("Loading state (selectScheduleAfterSuccess = true)") {
        val givenState = STATE.copy(selectScheduleAfterSuccess = true)
        When("News.News.GroupLoadedSuccessfully") {
            val (state, effects, actions) = reducer.reduce(News.GroupLoadedSuccessfully(CORRECT_NAME), givenState)
            Then("Check state") {
                state.isLoading.shouldBeFalse()
            }
            Then("Check effects") {
                effects.shouldContainExactly(
                    FindScheduleEffect.NavigateNextFragment(givenState.continueTo, CORRECT_NAME)
                )
            }
            Then("Check actions") {
                actions.shouldContainExactly(FindScheduleAction.SelectGroup(CORRECT_NAME))
            }
        }
    }
}) {
    private companion object {
        private val STATE = FindScheduleState(
            continueTo = ScheduleFeatureLauncher.ContinueTo.BACK,
            selectScheduleAfterSuccess = false
        )
        private const val CORRECT_NAME = "С-06-18"
        private const val INVALID_NAME = "С-vv--"
        private val VALIDATION_ERROR =
            CompositeException(HttpException(Response.error<Unit>(400, "".toResponseBody())))
        private val UNKNOWN_ERROR = IllegalStateException("Unknown error!")
    }
}