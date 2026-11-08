package kekmech.ru.feature_bars_impl.navigation

import kekmech.ru.lib_navigation.Command
import kekmech.ru.lib_navigation.ScreenKey
import kotlinx.parcelize.Parcelize

/** Known accounts and "Add account". Skipped when there are no accounts yet. */
@Parcelize
internal data object BarsAccountsScreenKey : ScreenKey

/** One login attempt. [accountId] is null for "Add account". */
@Parcelize
internal data class BarsLoginScreenKey(val accountId: String? = null) : ScreenKey

/** Closes the account list and the login screen, whichever of them are open. */
internal class CloseBarsLogin : Command {

    override fun apply(backStack: MutableList<ScreenKey>) {
        backStack.removeAll { it is BarsAccountsScreenKey || it is BarsLoginScreenKey }
    }
}

/** Details of the discipline at [index] of the current semester. */
@Parcelize
internal data class BarsDetailsScreenKey(val index: Int) : ScreenKey
