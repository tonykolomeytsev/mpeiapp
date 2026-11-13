package kekmech.ru.lib_navigation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

/**
 * Positive y is the finger moving down: the content scrolls to the top, the sheet is pulled down
 */
class SheetContentNestedScrollConnectionTest : BehaviorSpec({
    Given("Content is at the top") {
        When("Gesture pulls down and is released with velocity") {
            val connection = SheetContentNestedScrollConnection()
            val passedToSheet = connection.userScroll(consumed = 0f, available = 40f)
            val flingPassedToSheet = connection.release(available = 2000f)
            Then("Check the sheet is pulled down") {
                passedToSheet shouldBe 40f
            }
            Then("Check the sheet gets the velocity to settle or dismiss") {
                flingPassedToSheet shouldBe 2000f
            }
        }
    }
    Given("Content is scrolled down") {
        When("Gesture scrolls the content to the top and keeps pulling down") {
            val connection = SheetContentNestedScrollConnection()
            val beforeTop = connection.userScroll(consumed = 30f, available = 0f)
            val reachingTop = connection.userScroll(consumed = 10f, available = 20f)
            val afterTop = connection.userScroll(consumed = 0f, available = 40f)
            val flingPassedToSheet = connection.release(available = 2000f)
            Then("Check the content stops at the top and the sheet stays") {
                beforeTop shouldBe 0f
                reachingTop shouldBe 0f
                afterTop shouldBe 0f
            }
            Then("Check the sheet doesn't get the velocity") {
                flingPassedToSheet shouldBe 0f
            }
        }
        When("Content is flung to the top") {
            val connection = SheetContentNestedScrollConnection()
            connection.userScroll(consumed = 30f, available = 0f)
            connection.onPreFling(Velocity(0f, 3000f))
            val flingScrollPassedToSheet = connection.onPostScroll(
                consumed = Offset(0f, 100f),
                available = Offset(0f, 50f),
                source = NestedScrollSource.SideEffect,
            ).y
            val flingPassedToSheet = connection.passLeftoverFlingToSheet(available = 1500f)
            Then("Check the sheet isn't moved by the fling") {
                flingScrollPassedToSheet shouldBe 0f
                flingPassedToSheet shouldBe 0f
            }
        }
        When("Content is flung, and the next gesture starts at the top") {
            val connection = SheetContentNestedScrollConnection()
            connection.userScroll(consumed = 30f, available = 0f)
            connection.release(available = 1500f)
            val passedToSheet = connection.userScroll(consumed = 0f, available = 40f)
            Then("Check the next gesture pulls the sheet down") {
                passedToSheet shouldBe 40f
            }
        }
        When("Fling is interrupted by the next gesture which starts at the top") {
            val connection = SheetContentNestedScrollConnection()
            connection.userScroll(consumed = 30f, available = 0f)
            // The finger is up, the fling is cancelled by a touch before onPostFling
            connection.onPreFling(Velocity(0f, 3000f))
            val passedToSheet = connection.userScroll(consumed = 0f, available = 40f)
            Then("Check the next gesture pulls the sheet down") {
                passedToSheet shouldBe 40f
            }
        }
    }
    Given("Sheet is pulled down by the current gesture") {
        When("Gesture goes back up, scrolls the content and pulls down again") {
            val connection = SheetContentNestedScrollConnection()
            connection.userScroll(consumed = 0f, available = 40f)
            // The sheet takes the way back up itself, then the content scrolls
            connection.userScroll(consumed = -20f, available = 0f)
            val passedToSheet = connection.userScroll(consumed = 20f, available = 30f)
            Then("Check the sheet is still pulled by this gesture") {
                passedToSheet shouldBe 30f
            }
        }
    }
    Given("Content is at the bottom") {
        When("Gesture keeps pulling up") {
            val connection = SheetContentNestedScrollConnection()
            val passedToSheet = connection.userScroll(consumed = -30f, available = -20f)
            Then("Check the sheet gets the rest as usual") {
                passedToSheet shouldBe -20f
            }
        }
    }
})

/**
 * @return vertical scroll left for the sheet after this connection
 */
private fun SheetContentNestedScrollConnection.userScroll(consumed: Float, available: Float): Float {
    onPreScroll(Offset(0f, consumed + available), NestedScrollSource.UserInput)
    val consumedHere = onPostScroll(
        consumed = Offset(0f, consumed),
        available = Offset(0f, available),
        source = NestedScrollSource.UserInput,
    )
    return available - consumedHere.y
}

/**
 * Finger up, the content doesn't fling and leaves the whole [available] velocity
 *
 * @return vertical velocity left for the sheet after this connection
 */
private suspend fun SheetContentNestedScrollConnection.release(available: Float): Float {
    onPreFling(Velocity(0f, available))
    return passLeftoverFlingToSheet(available)
}

private suspend fun SheetContentNestedScrollConnection.passLeftoverFlingToSheet(available: Float): Float {
    val consumedHere = onPostFling(consumed = Velocity.Zero, available = Velocity(0f, available))
    return available - consumedHere.y
}
