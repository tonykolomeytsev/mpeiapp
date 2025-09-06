package kekmech.ru.feature_search_api

public interface SearchFeatureLauncher {
    public fun launch(query: String = "", filter: String = "ALL")
}
