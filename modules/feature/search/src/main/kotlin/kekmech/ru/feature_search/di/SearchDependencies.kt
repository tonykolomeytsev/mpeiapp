package kekmech.ru.feature_search.di

import kekmech.ru.domain_map.MapFeatureLauncher
import kekmech.ru.domain_notes.NotesFeatureLauncher
import kekmech.ru.feature_search.screens.main.elm.SearchStoreFactory

internal data class SearchDependencies(
    val searchStoreFactory: SearchStoreFactory,
    val notesFeatureLauncher: NotesFeatureLauncher,
    val mapFeatureLauncher: MapFeatureLauncher
)
