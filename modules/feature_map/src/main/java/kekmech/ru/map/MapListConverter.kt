package kekmech.ru.map

import kekmech.ru.coreui.items.PullItem
import kekmech.ru.coreui.items.SectionHeaderItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.domain_map.dto.MapMarker
import kekmech.ru.map.ext.toMarkerType
import kekmech.ru.map.items.TabBarItem
import kekmech.ru.map.presentation.FilterTab
import kekmech.ru.map.presentation.MapState

private const val TAB_BAR_ITEM_SPACING = 4

internal class MapListConverter {

    fun map(state: MapState): List<Any> {

        return mutableListOf<Any>().apply {
            add(PullItem)
            add(SpaceItem(0, TAB_BAR_ITEM_SPACING))
            add(TabBarItem)

            val selectedMarkers = state.markers.filter { it.type == state.selectedTab.toMarkerType() }
            if (selectedMarkers.all { it.tag.isEmpty() }) {
                createSectionHeader(state.selectedTab).let {
                    add(SpaceItem.VERTICAL_24)
                    add(it)
                    add(SpaceItem.VERTICAL_12)
                }
                addAll(selectedMarkers)
            } else {
                addAll(createListWithSections(selectedMarkers))
            }
        }
    }

    private fun createSectionHeader(selectedTab: FilterTab): Any = when (selectedTab) {
        FilterTab.FOOD -> SectionHeaderItem(
            titleRes = R.string.map_section_name_eat
        )
        FilterTab.BUILDINGS -> SectionHeaderItem(
            titleRes = R.string.map_section_name_buildings
        )
        FilterTab.HOSTELS -> SectionHeaderItem(
            titleRes = R.string.map_section_name_hostels
        )
        FilterTab.OTHERS -> SectionHeaderItem(
            titleRes = R.string.map_section_name_others
        )
        FilterTab.STRUCTURES -> SectionHeaderItem(
            titleRes = R.string.map_section_name_structures
        )
    }

    private fun createListWithSections(markers: List<MapMarker>): List<Any> {
        val setOfTags = markers.map { it.tag }.toSet()
        val list = mutableListOf<Any>()
        for (tag in setOfTags) {
            if (tag.isEmpty()) continue
            list.add(SpaceItem.VERTICAL_24)
            list.add(SectionHeaderItem(title = tag))
            list.add(SpaceItem.VERTICAL_12)
            list.addAll(markers.filter { it.tag == tag })
        }
        val otherPlaces = markers.filter { it.tag.isEmpty() }
        if (otherPlaces.isNotEmpty()) {
            list.add(SpaceItem.VERTICAL_24)
            list.add(SectionHeaderItem(titleRes = R.string.map_section_tag_others))
            list.add(SpaceItem.VERTICAL_12)
            list.addAll(otherPlaces)
        }
        return list
    }
}