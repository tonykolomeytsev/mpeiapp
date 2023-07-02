package kekmech.ru.mpeiapp.deeplink.handlers

import android.net.Uri
import kekmech.ru.domain_search.SearchFeatureLauncher
import kekmech.ru.library_navigation.BottomTab
import kekmech.ru.library_navigation.BottomTabsSwitcher
import kekmech.ru.library_navigation.PopUntil
import kekmech.ru.library_navigation.Router
import kekmech.ru.mpeiapp.deeplink.DeeplinkHandler
import kekmech.ru.mpeiapp.ui.main.MainFragment

class SearchScreenDeeplinkHandler(
    private val router: Router,
    private val bottomTabsSwitcher: BottomTabsSwitcher,
    private val searchFeatureLauncher: SearchFeatureLauncher
) : DeeplinkHandler("search") {

    override fun handle(deeplink: Uri) {
        val searchQuery = deeplink.getQueryParameter("q").orEmpty()
        val searchFilter = deeplink.getQueryParameter("filter") ?: "ALL"
        router.executeCommand(PopUntil(MainFragment::class, inclusive = false))
        bottomTabsSwitcher.changeTab(BottomTab.DASHBOARD)
        searchFeatureLauncher.launch(searchQuery, searchFilter)
    }
}
