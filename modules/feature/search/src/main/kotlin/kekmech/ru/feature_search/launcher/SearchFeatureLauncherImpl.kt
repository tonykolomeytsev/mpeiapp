package kekmech.ru.feature_search.launcher

import kekmech.ru.domain_search.SearchFeatureLauncher
import kekmech.ru.feature_search.screens.main.SearchFragment
import kekmech.ru.library_navigation.AddScreenForward
import kekmech.ru.library_navigation.Router

internal class SearchFeatureLauncherImpl(
    private val router: Router
) : SearchFeatureLauncher {

    override fun launch(query: String, filter: String) {
        router.executeCommand(AddScreenForward { SearchFragment.newInstance(query, filter) })
    }
}
