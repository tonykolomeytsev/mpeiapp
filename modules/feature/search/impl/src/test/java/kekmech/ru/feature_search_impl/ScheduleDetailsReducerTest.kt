package kekmech.ru.feature_search_impl

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule
import kekmech.ru.feature_schedule_api.domain.model.Schedule
import kekmech.ru.feature_schedule_api.domain.model.ScheduleKey
import kekmech.ru.feature_schedule_api.domain.model.ScheduleRefreshState
import kekmech.ru.feature_schedule_api.domain.model.ScheduleResource
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.SearchResult
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsCommand
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsEvent.Internal
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsEvent.Ui
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsReducer
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsState
import java.time.Instant
import java.time.LocalDate
import java.time.Month

class ScheduleDetailsReducerTest : BehaviorSpec({
    val reducer = ScheduleDetailsReducer()
    Given("Initial state, Tuesday") {
        val givenState = STATE
        When("Wish.Init") {
            val (state, effects, actions) = reducer.reduce(Ui.Init, givenState)
            Then("Check state") {
                state shouldBe givenState
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check this and next weeks and the favorites are loaded") {
                actions.shouldContainExactly(
                    observeSchedule(0),
                    observeSchedule(1),
                    ScheduleDetailsCommand.GetFavorites,
                )
            }
        }
        When("Wish.Click.Day") {
            val (state, effects, actions) = reducer.reduce(
                Ui.Click.Day(TUESDAY.plusDays(2)),
                givenState.copy(isFavoritesFabVisible = false),
            )
            Then("Check state") {
                state.selectedDate shouldBe TUESDAY.plusDays(2)
                state.isFavoritesFabVisible.shouldBeTrue()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("Wish.Action.SelectWeek (2)") {
            val (state, effects, actions) = reducer.reduce(Ui.Action.SelectWeek(2), givenState)
            Then("Check the same day of the week is selected") {
                state.weekOffset shouldBe 2
                state.selectedDate shouldBe TUESDAY.plusWeeks(2)
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check the week is observed") {
                actions.shouldContainExactly(observeSchedule(2))
            }
        }
        When("Wish.Action.SelectWeek (the selected one)") {
            val (state, effects, actions) = reducer.reduce(Ui.Action.SelectWeek(0), givenState)
            Then("Check nothing happens") {
                state shouldBe givenState
                effects.shouldBeEmpty()
                actions.shouldBeEmpty()
            }
        }
        When("Wish.Action.PageChanged (same week)") {
            val (state, effects, actions) = reducer.reduce(
                Ui.Action.PageChanged(weekOffset = 0, dayOfWeek = 4),
                givenState.copy(isFavoritesFabVisible = false),
            )
            Then("Check Thursday is selected") {
                state.weekOffset shouldBe 0
                state.selectedDate shouldBe TUESDAY.plusDays(2)
                state.isFavoritesFabVisible.shouldBeTrue()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("Wish.Action.PageChanged (past Saturday to the next Monday)") {
            val (state, _, actions) = reducer.reduce(
                Ui.Action.PageChanged(weekOffset = 1, dayOfWeek = 1),
                givenState,
            )
            Then("Check the next week is selected") {
                state.weekOffset shouldBe 1
                state.selectedDate shouldBe NEXT_MONDAY
            }
            Then("Check the next week is observed") {
                actions.shouldContainExactly(observeSchedule(1))
            }
        }
        When("Wish.Action.PageChanged (past Monday to the previous Saturday)") {
            val (state, _, actions) = reducer.reduce(
                Ui.Action.PageChanged(weekOffset = -1, dayOfWeek = 6),
                givenState,
            )
            Then("Check the previous week is selected") {
                state.weekOffset shouldBe -1
                state.selectedDate shouldBe THIS_MONDAY.minusDays(2)
            }
            Then("Check the previous week is observed") {
                actions.shouldContainExactly(observeSchedule(-1))
            }
        }
        When("Wish.Action.ClassesScrolled (scrolled down)") {
            val (state, _, _) = reducer.reduce(Ui.Action.ClassesScrolled(10), givenState)
            Then("Check the favorites button hides") {
                state.isFavoritesFabVisible.shouldBeFalse()
            }
        }
        When("Wish.Action.ClassesScrolled (scrolled up)") {
            val (state, _, _) = reducer.reduce(
                Ui.Action.ClassesScrolled(-10),
                givenState.copy(isFavoritesFabVisible = false),
            )
            Then("Check the favorites button shows") {
                state.isFavoritesFabVisible.shouldBeTrue()
            }
        }
        When("Wish.Click.Favorites") {
            val (state, effects, actions) = reducer.reduce(Ui.Click.Favorites, givenState)
            Then("Check state") {
                state.isInFavorites shouldBe null
                state.isFavoritesUpdating shouldBe true
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldContainExactly(ScheduleDetailsCommand.AddToFavorites(FAVORITE))
            }
        }
        When("News.ScheduleUpdated") {
            val (state, effects, actions) = reducer.reduce(
                Internal.ScheduleUpdated(weekOffset = 0, resource = RESOURCE),
                givenState.copy(observeErrors = mapOf(0 to ERROR)),
            )
            Then("Check the week is saved and its error is cleared") {
                state.schedule.shouldContainExactly(mapOf(0 to RESOURCE))
                state.observeErrors.shouldBeEmpty()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("News.ObserveScheduleFailure") {
            val (state, _, actions) = reducer.reduce(
                Internal.ObserveScheduleFailure(weekOffset = 1, throwable = ERROR),
                givenState,
            )
            Then("Check the error is saved") {
                state.observeErrors.shouldContainExactly(mapOf(1 to ERROR))
                state.loadingError(1) shouldBe ERROR
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("News.FavoritesLoaded (true)") {
            val (state, effects, actions) = reducer.reduce(
                Internal.GetFavoritesSuccess(listOf(FAVORITE)),
                givenState,
            )
            Then("Check state") {
                state.isInFavorites shouldBe true
                state.favoriteSchedule shouldBe FAVORITE
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("News.FavoritesLoaded (false)") {
            val (state, effects, actions) =
                reducer.reduce(Internal.GetFavoritesSuccess(listOf(FAVORITE_ANOTHER)), givenState)
            Then("Check state") {
                state.isInFavorites shouldBe false
                state.favoriteSchedule.shouldBeNull()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("News.FavoriteRemoved") {
            val (state, effects, actions) = reducer.reduce(
                Internal.RemoveFromFavoritesSuccess,
                givenState,
            )
            Then("Check state") {
                state.isInFavorites shouldBe false
                state.favoriteSchedule.shouldBeNull()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("News.FavoriteAdded") {
            val (state, effects, actions) = reducer.reduce(
                Internal.AddToFavoritesSuccess(FAVORITE),
                givenState,
            )
            Then("Check state") {
                state.isInFavorites shouldBe true
                state.favoriteSchedule shouldBe FAVORITE
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
    }
    Given("Initial state, Saturday") {
        val givenState = STATE.copy(selectedDate = THIS_MONDAY.plusDays(5))
        When("Wish.Init") {
            val (state, _, actions) = reducer.reduce(Ui.Init, givenState)
            Then("Check Monday of the next week is selected, like in the Schedule tab") {
                state.selectedDate shouldBe NEXT_MONDAY
                state.weekOffset shouldBe 1
            }
            Then("Check this and next weeks and the favorites are loaded") {
                actions.shouldContainExactly(
                    observeSchedule(0),
                    observeSchedule(1),
                    ScheduleDetailsCommand.GetFavorites,
                )
            }
        }
    }
    Given("Initial state, Sunday") {
        val givenState = STATE.copy(selectedDate = THIS_MONDAY.plusDays(6))
        When("Wish.Init") {
            val (state, _, _) = reducer.reduce(Ui.Init, givenState)
            Then("Check Monday of the next week is selected") {
                state.selectedDate shouldBe NEXT_MONDAY
                state.weekOffset shouldBe 1
            }
        }
    }
    Given("The selected week is loaded") {
        val givenState = STATE.copy(schedule = mapOf(0 to RESOURCE))
        When("Ui.Click.Reload") {
            val (state, _, actions) = reducer.reduce(Ui.Click.Reload, givenState)
            Then("Check the alive subscription is refreshed") {
                state shouldBe givenState
                actions.shouldContainExactly(ScheduleDetailsCommand.RefreshSchedule(RESOURCE.key))
            }
        }
    }
    Given("Subscription to the selected week failed") {
        val givenState = STATE.copy(observeErrors = mapOf(0 to ERROR))
        When("Ui.Click.Reload") {
            val (state, _, actions) = reducer.reduce(Ui.Click.Reload, givenState)
            Then("Check the error is cleared") {
                state.observeErrors.shouldBeEmpty()
            }
            Then("Subscribe again") {
                actions.shouldContainExactly(observeSchedule(0))
            }
        }
    }
    Given("Favorite loaded (non-empty)") {
        val givenState = STATE.copy(isInFavorites = true, favoriteSchedule = FAVORITE)
        When("Wish.Click.Favorites") {
            val (state, effects, actions) = reducer.reduce(Ui.Click.Favorites, givenState)
            Then("Check the previous value is kept while favorites update") {
                state.isInFavorites shouldBe true
                state.isFavoritesUpdating shouldBe true
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldContainExactly(ScheduleDetailsCommand.RemoveFromFavorites(FAVORITE))
            }
        }
    }
    Given("Favorites are updating") {
        val givenState = STATE.copy(isInFavorites = false, isFavoritesUpdating = true)
        When("Wish.Click.Favorites") {
            val (state, effects, actions) = reducer.reduce(Ui.Click.Favorites, givenState)
            Then("Check repeated click is ignored") {
                state shouldBe givenState
                effects.shouldBeEmpty()
                actions.shouldBeEmpty()
            }
        }
        When("News.FavoriteAdded") {
            val (state, _, _) = reducer.reduce(Internal.AddToFavoritesSuccess(FAVORITE), givenState)
            Then("Check update is finished") {
                state.isInFavorites shouldBe true
                state.isFavoritesUpdating shouldBe false
                state.favoriteSchedule shouldBe FAVORITE
            }
        }
        When("News.FavoriteRemoved") {
            val (state, _, _) = reducer.reduce(
                Internal.RemoveFromFavoritesSuccess,
                givenState.copy(isInFavorites = true, favoriteSchedule = FAVORITE),
            )
            Then("Check update is finished") {
                state.isInFavorites shouldBe false
                state.isFavoritesUpdating shouldBe false
                state.favoriteSchedule.shouldBeNull()
            }
        }
    }
}) {

    private companion object {

        private val SEARCH_RESULT = SearchResult(
            id = "id",
            name = "Зуев Юрий Юрьевич",
            description = "",
            type = ScheduleType.PERSON,
        )
        private val THIS_MONDAY = LocalDate.of(2026, Month.NOVEMBER, 9)
        private val TUESDAY = THIS_MONDAY.plusDays(1)
        private val NEXT_MONDAY = THIS_MONDAY.plusWeeks(1)
        private val STATE = ScheduleDetailsState(SEARCH_RESULT, selectedDate = TUESDAY)
        private val FAVORITE = FavoriteSchedule(
            name = SEARCH_RESULT.name,
            type = ScheduleType.PERSON,
            description = SEARCH_RESULT.description,
            order = 0
        )
        private val FAVORITE_ANOTHER = FavoriteSchedule(
            name = "Василий Пупкин",
            type = ScheduleType.PERSON,
            description = "",
            order = 0
        )
        private val ERROR = IllegalStateException()
        private val RESOURCE = ScheduleResource(
            key = ScheduleKey(ScheduleType.PERSON, SEARCH_RESULT.name, THIS_MONDAY),
            value = Schedule(name = SEARCH_RESULT.name, id = "12345", type = ScheduleType.PERSON, weeks = emptyList()),
            updatedAt = Instant.parse("2026-11-09T10:15:30Z"),
            refresh = ScheduleRefreshState.Idle,
        )

        private fun observeSchedule(weekOffset: Int) = ScheduleDetailsCommand.ObserveSchedule(
            type = SEARCH_RESULT.type,
            name = SEARCH_RESULT.name,
            weekOffset = weekOffset,
        )
    }
}
