package kekmech.ru.feature_search_impl.launcher

import kekmech.ru.feature_search_api.SearchFeatureLauncher
import kekmech.ru.feature_search_impl.navigation.SearchScreenKey
import kekmech.ru.lib_navigation.AddScreenForward
import kekmech.ru.lib_navigation.Router

internal class SearchFeatureLauncherImpl(
    private val router: Router
) : SearchFeatureLauncher {

    override fun launch(query: String, filter: String) {
        router.executeCommand(AddScreenForward(SearchScreenKey(query, filter)))
    }
}
