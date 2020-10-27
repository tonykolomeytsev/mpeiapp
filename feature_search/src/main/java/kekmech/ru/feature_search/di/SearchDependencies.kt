package kekmech.ru.feature_search.di

import kekmech.ru.feature_search.mvi.SearchFeatureFactory

internal data class SearchDependencies(
    val searchFeatureFactory: SearchFeatureFactory
)