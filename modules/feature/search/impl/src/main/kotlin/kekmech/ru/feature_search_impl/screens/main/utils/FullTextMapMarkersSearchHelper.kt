package kekmech.ru.feature_search_impl.screens.main.utils

import kekmech.ru.feature_map_api.domain.model.MapMarker
import kekmech.ru.feature_search_impl.screens.main.simplify

internal class FullTextMapMarkersSearchHelper(
    private val mapMarkers: List<MapMarker>,
    private val query: String,
) {

    fun execute() = mapMarkers
        .filter { note -> predicates.any { it(note, query) } }

    companion object {

        private val predicates = listOf<(MapMarker, String) -> Boolean>(
            { n, q -> q in n.name.simplify() },
            { n, q -> q in n.address.simplify() },
            { n, q -> n.tag?.let { q in it.simplify() } ?: false }
        )
    }
}
