package kekmech.ru.mpeiapp.ui.main

import kekmech.ru.common_navigation.BottomTab
import java.util.*

class BottomBarBackStack(
    private val firstTab: BottomTab
) {
    private val backStack: Deque<BottomTab> = ArrayDeque()

    fun popAndPeek(): BottomTab? {
        if (backStack.size == 1 && backStack.first() == firstTab) {
            return null
        }

        if (backStack.isNotEmpty()) {
            backStack.pop()
        }
        return backStack.peek()
    }

    fun push(tab: BottomTab) {
        if (tab == firstTab) {
            backStack.clear()
        } else {
            backStack.remove(tab)
        }
        backStack.push(tab)
    }
}