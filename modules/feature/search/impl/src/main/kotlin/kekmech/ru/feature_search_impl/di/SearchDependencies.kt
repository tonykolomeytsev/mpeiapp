package kekmech.ru.feature_search_impl.di

import kekmech.ru.feature_map_api.MapFeatureLauncher
import kekmech.ru.feature_notes_api.NotesFeatureLauncher
import kekmech.ru.feature_search_impl.screens.main.elm.SearchStoreFactory

internal data class SearchDependencies(
    val searchStoreFactory: SearchStoreFactory,
    val notesFeatureLauncher: NotesFeatureLauncher,
    val mapFeatureLauncher: MapFeatureLauncher
)
