package kekmech.ru.feature_search

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kekmech.ru.domain_schedule.dto.*
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsAction
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsEffect
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsEvent.News
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsEvent.Wish
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsReducer
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsState
import java.time.LocalDate
import java.time.Month

class ScheduleDetailsReducerTest : BehaviorSpec({
    val reducer = ScheduleDetailsReducer()
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
                actions.shouldContainExactly(
                    ScheduleDetailsAction.LoadSchedule(SEARCH_RESULT.name, 0),
                    ScheduleDetailsAction.LoadSchedule(SEARCH_RESULT.name, 1),
                    ScheduleDetailsAction.LoadFavorites
                )
            }
        }
        When("Wish.Click.Day") {
            val (state, effects, actions) = reducer.reduce(Wish.Click.Day(SELECTED_DATE), givenState)
            Then("Check state") {
                state.selectedDayDate shouldBe SELECTED_DATE
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("Wish.Click.Favorites") {
            val (state, effects, actions) = reducer.reduce(Wish.Click.Favorites, givenState)
            Then("Check state") {
                state.isInFavorites shouldBe null
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldContainExactly(ScheduleDetailsAction.AddToFavorites(FAVORITE))
            }
        }
        When("Wish.Click.SwitchSchedule") {
            val (state, effects, actions) = reducer.reduce(Wish.Click.SwitchSchedule, givenState)
            Then("Check state") {
                state shouldBe givenState
            }
            Then("Check effects") {
                effects.shouldContainExactly(ScheduleDetailsEffect.CloseAndGoToSchedule)
            }
            Then("Check actions") {
                actions.shouldContainExactly(ScheduleDetailsAction.SwitchSchedule(SEARCH_RESULT.name))
            }
        }
        When("News.ScheduleLoaded (0)") {
            val (state, effects, actions) = reducer.reduce(News.ScheduleLoaded(SCHEDULE_0, 0), givenState)
            Then("Check state") {
                state.thisWeek.shouldContainExactly(SCHEDULE_0.mapToDays())
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("News.ScheduleLoaded (1)") {
            val (state, effects, actions) = reducer.reduce(News.ScheduleLoaded(SCHEDULE_0, 1), givenState)
            Then("Check state") {
                state.nextWeek.shouldContainExactly(SCHEDULE_0.mapToDays())
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("News.LoadScheduleError (0)") {
            val (state, effects, actions) = reducer.reduce(News.LoadScheduleError(0), givenState)
            Then("Check state") {
                state.thisWeek shouldBe emptyList()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("News.LoadScheduleError (1)") {
            val (state, effects, actions) = reducer.reduce(News.LoadScheduleError(1), givenState)
            Then("Check state") {
                state.nextWeek shouldBe emptyList()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("News.FavoritesLoaded (true)") {
            val (state, effects, actions) = reducer.reduce(News.FavoritesLoaded(listOf(FAVORITE)), givenState)
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
                reducer.reduce(News.FavoritesLoaded(listOf(FAVORITE_ANOTHER)), givenState)
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
            val (state, effects, actions) = reducer.reduce(News.FavoriteRemoved, givenState)
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
            val (state, effects, actions) = reducer.reduce(News.FavoriteAdded(FAVORITE), givenState)
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
    Given("Favorite loaded (non-empty)") {
        val givenState = STATE.copy(isInFavorites = true, favoriteSchedule = FAVORITE)
        When("Wish.Click.Favorites") {
            val (state, effects, actions) = reducer.reduce(Wish.Click.Favorites, givenState)
            Then("Check state") {
                state.isInFavorites shouldBe null
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldContainExactly(ScheduleDetailsAction.RemoveFromFavorites(FAVORITE))
            }
        }
    }
}) {
    private companion object {
        private val SEARCH_RESULT = SearchResult(
            id = "id",
            name = "Зуев Юрий Юрьевич",
            description = "",
            type = SearchResultType.PERSON
        )
        private val STATE = ScheduleDetailsState(SEARCH_RESULT)
        private val SELECTED_DATE = LocalDate.of(2020, 1, 1)
        private val FAVORITE = FavoriteSchedule(
            groupNumber = SEARCH_RESULT.name,
            description = SEARCH_RESULT.description,
            order = 0
        )
        private val FAVORITE_ANOTHER = FavoriteSchedule(
            groupNumber = "Василий Пупкин",
            description = "",
            order = 0
        )
        private val CURRENT_MONDAY = LocalDate.of(2020, Month.SEPTEMBER, 14)
        private val SCHEDULE_0 = Schedule(
            name = "C-12-16",
            id = "12345",
            type = ScheduleType.GROUP,
            weeks = listOf(
                Week(
                weekOfSemester = 3,
                weekOfYear = 36,
                firstDayOfWeek = CURRENT_MONDAY,
                days = listOf(
                    Day(
                        dayOfWeek = 1,
                        date = CURRENT_MONDAY,
                        classes = listOf()
                    )
                )
            )
            )
        )

        private fun Schedule.mapToDays(): List<Day> {
            val week = weeks.firstOrNull() ?: return emptyList()
            val days = weeks.flatMap { it.days }
            return (1..7)
                .map { dayOfWeek ->
                    days.find { it.dayOfWeek == dayOfWeek }
                        ?: Day(dayOfWeek, week.firstDayOfWeek.plusDays(dayOfWeek - 1L))
                }
        }
    }
}