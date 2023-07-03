package kekmech.ru.feature_search_api

interface SearchFeatureLauncher {
    fun launch(query: String = "", filter: String = "ALL")
}
