package kekmech.ru.feature_map_impl.presentation.screen.main

import androidx.annotation.StringRes
import kekmech.ru.feature_map_api.domain.model.MapMarker
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.FilterTab
import kekmech.ru.feature_map_impl.presentation.screen.main.ext.toMarkerType
import kekmech.ru.res_strings.R.string as Strings

internal sealed interface SectionTitle {
    data class Text(val value: String) : SectionTitle
    data class Res(@StringRes val id: Int) : SectionTitle
}

internal data class MapSection(
    val title: SectionTitle,
    val markers: List<MapMarker>,
)

/**
 * Splits markers of the selected tab into sections by tag.
 * Markers without a tag go to the last section, or to the only one if no marker has a tag.
 */
internal fun createMapSections(markers: List<MapMarker>, selectedTab: FilterTab): List<MapSection> {
    val selectedMarkers = markers.filter { it.type == selectedTab.toMarkerType() }
    if (selectedMarkers.all { it.tag.isNullOrEmpty() }) {
        return listOf(MapSection(SectionTitle.Res(selectedTab.sectionNameRes), selectedMarkers))
    }
    val taggedSections = selectedMarkers
        .filter { !it.tag.isNullOrEmpty() }
        .groupBy { it.tag.orEmpty() }
        .map { (tag, taggedMarkers) -> MapSection(SectionTitle.Text(tag), taggedMarkers) }
    val otherPlaces = selectedMarkers.filter { it.tag.isNullOrEmpty() }
    return if (otherPlaces.isEmpty()) {
        taggedSections
    } else {
        taggedSections + MapSection(SectionTitle.Res(Strings.map_section_tag_others), otherPlaces)
    }
}

private val FilterTab.sectionNameRes: Int
    get() = when (this) {
        FilterTab.FOOD -> Strings.map_section_name_eat
        FilterTab.BUILDINGS -> Strings.map_section_name_buildings
        FilterTab.HOSTELS -> Strings.map_section_name_hostels
        FilterTab.OTHERS -> Strings.map_section_name_others
        FilterTab.STRUCTURES -> Strings.map_section_name_structures
    }
