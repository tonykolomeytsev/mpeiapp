package kekmech.ru.feature_search.screens.main.utils

import kekmech.ru.domain_map.dto.MapMarker
import kekmech.ru.feature_search.screens.main.simplify

internal class FullTextMapMarkersSearchHelper(
    private val mapMarkers: List<MapMarker>,
    private val query: String
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
