package kekmech.ru.feature_search.di

import kekmech.ru.domain_map.MapFeatureLauncher
import kekmech.ru.domain_notes.NotesFeatureLauncher
import kekmech.ru.feature_search.main.elm.SearchFeatureFactory

internal data class SearchDependencies(
    val searchFeatureFactory: SearchFeatureFactory,
    val notesFeatureLauncher: NotesFeatureLauncher,
    val mapFeatureLauncher: MapFeatureLauncher
)
