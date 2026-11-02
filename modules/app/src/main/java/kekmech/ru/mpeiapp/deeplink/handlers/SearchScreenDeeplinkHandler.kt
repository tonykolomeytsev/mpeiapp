package kekmech.ru.mpeiapp.deeplink.handlers

import android.net.Uri
import kekmech.ru.feature_search_api.SearchFeatureLauncher
import kekmech.ru.lib_navigation.BottomTab
import kekmech.ru.lib_navigation.BottomTabsSwitcher
import kekmech.ru.mpeiapp.deeplink.DeeplinkHandler

class SearchScreenDeeplinkHandler(
    private val bottomTabsSwitcher: BottomTabsSwitcher,
    private val searchFeatureLauncher: SearchFeatureLauncher
) : DeeplinkHandler("search") {

    override fun handle(deeplink: Uri) {
        val searchQuery = deeplink.getQueryParameter("q").orEmpty()
        val searchFilter = deeplink.getQueryParameter("filter") ?: "ALL"
        bottomTabsSwitcher.changeTab(BottomTab.DASHBOARD)
        searchFeatureLauncher.launch(searchQuery, searchFilter)
    }
}
