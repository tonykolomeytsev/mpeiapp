package kekmech.ru.library_navigation_compose

import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.pop
import com.bumble.appyx.navmodel.backstack.operation.push
import com.bumble.appyx.navmodel.backstack.operation.replace
import kekmech.ru.library_navigation_api.NavTarget

/**
 * Handle to control backstack inside screens
 *
 * @see BackStack
 * @see BackStackNavigator.back
 * @see BackStackNavigator.navigate
 * @see BackStackNavigator.replace
 */
class BackStackNavigator(private val backStack: BackStack<NavTarget>) {

    /**
     * Return to previous screen. Pop backstack.
     *
     * Before `.back()`:
     * ```kotlin
     * [A, B, C]
     * //     ^current screen
     * ```
     *
     * After `.back()`:
     * ```kotlin
     * [A, B]
     * //  ^new current screen
     * ```
     */
    fun back() {
        backStack.pop()
    }

    /**
     * Navigate to the new screen. Push to backstack.
     *
     * Before `.navigate(D)`:
     * ```kotlin
     * [A, B, C]
     * //     ^current screen
     * ```
     *
     * After `.navigate(D)`:
     * ```kotlin
     * [A, B, C, D]
     * //        ^new current screen
     * ```
     */
    fun navigate(navTarget: NavTarget) {
        backStack.push(navTarget)
    }

    /**
     * Replace current screen. Replace top item in backstack.
     *
     * Before `.replace(D)`:
     * ```kotlin
     * [A, B, C]
     * //     ^current screen
     * ```
     *
     * After `.replace(D)`:
     * ```kotlin
     * [A, B, D]
     * //     ^new current screen
     * ```
     */
    fun replace(navTarget: NavTarget) {
        backStack.replace(navTarget)
    }
}