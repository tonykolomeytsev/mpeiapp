package kekmech.ru.feature_search.launcher

import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.Router
import kekmech.ru.domain_search.SearchFeatureLauncher
import kekmech.ru.feature_search.SearchFragment

internal class SearchFeatureLauncherImpl(
    private val router: Router
) : SearchFeatureLauncher {

    override fun launch() {
        router.executeCommand(AddScreenForward { SearchFragment() })
    }
}