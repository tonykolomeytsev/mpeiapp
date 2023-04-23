package kekmech.ru.feature_app_settings.screens.favorites.elm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kekmech.ru.domain_schedule.dto.FavoriteSchedule
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesEvent.Internal
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesEvent.Ui

internal class FavoritesReducerTest : BehaviorSpec({
    val reducer = FavoritesReducer()

    Given("Initial state") {
        val givenState = FavoritesState()
        When("Ui.Init") {
            val (state, effects, commands) = reducer.reduce(Ui.Init, givenState)
            Then("Same state") {
                state.shouldBe(givenState)
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("Check commands") {
                commands.shouldContainExactly(listOf(FavoritesCommand.LoadAllFavorites))
            }
        }
        When("Internal.LoadAllFavoritesSuccess") {
            val (state, effects, commands) = reducer.reduce(
                Internal.LoadAllFavoritesSuccess(Mocks.FAVORITES_LIST),
                givenState
            )
            Then("New state") {
                state.shouldBe(state.copy(favorites = Mocks.FAVORITES_LIST))
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("No commands") {
                commands.shouldBeEmpty()
            }
        }
    }
    Given("State after favorites loading") {
        val givenState = FavoritesState(favorites = Mocks.FAVORITES_LIST)
        When("Ui.Action.UpdateFavorite (with new favorite)") {
            val (state, effects, commands) = reducer.reduce(
                Ui.Action.UpdateFavorite(Mocks.NEW_FAVORITE),
                givenState
            )
            val newFavorites = Mocks.FAVORITES_LIST + listOf(Mocks.NEW_FAVORITE)
            Then("New state") {
                state.shouldBe(state.copy(favorites = newFavorites))
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("Check commands") {
                commands.shouldContainExactly(listOf(FavoritesCommand.SetFavorites(newFavorites)))
            }
        }
        When("Ui.Action.UpdateFavorite (with updated favorite)") {
            val updatedFavorite = Mocks.FAVORITES_LIST.first().copy(description = "New description")
            val (state, effects, commands) = reducer.reduce(
                event = Ui.Action.UpdateFavorite(updatedFavorite),
                state = givenState
            )
            val newFavorites = Mocks.FAVORITES_LIST.mapIndexed { index, favoriteSchedule ->
                if (index == 0) updatedFavorite else favoriteSchedule
            }
            Then("New state") {
                state.shouldBe(state.copy(favorites = newFavorites))
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("Check commands") {
                commands.shouldContainExactly(listOf(FavoritesCommand.SetFavorites(newFavorites)))
            }
        }
        When("Ui.Click.DeleteFavorite") {
            val deletedFavorite = Mocks.FAVORITES_LIST.first().copy(description = "New description")
            val (state, effects, commands) = reducer.reduce(
                event = Ui.Click.DeleteFavorite(deletedFavorite),
                state = givenState
            )
            val newFavorites = Mocks.FAVORITES_LIST.filterIndexed { index, _ -> index > 0 }
            Then("New state") {
                state.shouldBe(state.copy(favorites = newFavorites))
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("Check commands") {
                commands.shouldContainExactly(listOf(FavoritesCommand.SetFavorites(newFavorites)))
            }
        }
    }
})

private object Mocks {
    val FAVORITES_LIST =
        listOf(
            FavoriteSchedule(
                groupNumber = "A-08-22",
                description = "Hello world",
                order = 0,
            ),
            FavoriteSchedule(
                groupNumber = "C-08-22",
                description = "Hello world!",
                order = 1,
            )
        )
    val NEW_FAVORITE =
        FavoriteSchedule(
            groupNumber = "Cэ-12-22",
            description = "Hello world!",
            order = 1,
        )
}
