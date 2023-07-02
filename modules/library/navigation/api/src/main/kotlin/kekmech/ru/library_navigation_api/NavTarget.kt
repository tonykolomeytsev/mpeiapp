package kekmech.ru.library_navigation_api

import android.os.Parcelable
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node

/**
 * Navigation target for global backstack model navigation.
 * Implement this interface to create a screen that can be navigated to inside the backstack.
 *
 * Usage:
 * ```kotlin
 * @Parcelize
 * internal class SomeScreenNavTarget(private val arg: SomeArg) : NavTarget {
 *
 *     override fun resolve(buildContext: BuildContext): Node =
 *         node(buildContext) { SomeScreen(arg) }
 * }
 * ```
 */
interface NavTarget : Parcelable {

    fun resolve(buildContext: BuildContext): Node
}
