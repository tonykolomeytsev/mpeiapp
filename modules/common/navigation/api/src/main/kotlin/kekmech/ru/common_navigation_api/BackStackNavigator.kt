package kekmech.ru.common_navigation_api

import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.pop
import com.bumble.appyx.navmodel.backstack.operation.push
import com.bumble.appyx.navmodel.backstack.operation.replace

class BackStackNavigator(private val backStack: BackStack<NavTarget>) {

    fun pop(){
        backStack.pop()
    }

    fun push(navTarget: NavTarget) {
        backStack.push(navTarget)
    }

    fun replace(navTarget: NavTarget) {
        backStack.replace(navTarget)
    }
}
