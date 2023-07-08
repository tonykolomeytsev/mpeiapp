package kekmech.ru.feature_main_screen_api.presentation.navigation

/**
 * Switching tabs is not "navigation" in its truest sense, it is the functionality of the
 * main screen. Hence `lib_navigation` does not know about any tabs. Anyone who wants to switch
 * main screen tabs must depend on the main acreen api and this interface.
 */
interface MainScreenTabSwitcher {

    fun switch(tab: MainScreenTab)
}
