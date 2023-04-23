package kekmech.ru.feature_app_settings.screens.favorites.elm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

internal class FavoritesReducerTest : BehaviorSpec({
    val reducer = FavoritesReducer()

    Given("Initial state") {
        val givenState = FavoritesState()
        When("Ui.Init") {
            val (state, effects, actions) = reducer.reduce(FavoritesEvent.Ui.Init, givenState)
            Then("Same state") {
                state.shouldBe(givenState)
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("Check commands") {
                actions.shouldContainExactly(listOf(FavoritesCommand.LoadAllFavorites))
            }
        }
    }
})