package kekmech.ru.feature_map_impl.presentation.screen.main

import kekmech.ru.coreui.items.ErrorStateItem
import kekmech.ru.coreui.items.PullItem
import kekmech.ru.coreui.items.SectionHeaderItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.feature_map_api.domain.model.MapMarker
import kekmech.ru.feature_map_impl.presentation.items.TabBarItem
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.FilterTab
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapState
import kekmech.ru.feature_map_impl.presentation.screen.main.ext.toMarkerType
import kekmech.ru.res_strings.R.string as Strings

private const val TAB_BAR_ITEM_SPACING = 4

internal class MapListConverter {

    @Suppress("NestedBlockDepth")
    fun map(state: MapState): List<Any> {
        return mutableListOf<Any>().apply {
            add(PullItem)
            add(SpaceItem(0, TAB_BAR_ITEM_SPACING))
            add(TabBarItem)

            if (state.markers.isEmpty() && state.loadingError != null) {
                add(SpaceItem.VERTICAL_24)
                add(ErrorStateItem(state.loadingError))
            } else {
                val selectedMarkers =
                    state.markers.filter { it.type == state.selectedTab.toMarkerType() }
                if (selectedMarkers.all { it.tag.isNullOrEmpty() }) {
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
    }

    private fun createSectionHeader(selectedTab: FilterTab): Any = when (selectedTab) {
        FilterTab.FOOD -> SectionHeaderItem(
            titleRes = Strings.map_section_name_eat
        )
        FilterTab.BUILDINGS -> SectionHeaderItem(
            titleRes = Strings.map_section_name_buildings
        )
        FilterTab.HOSTELS -> SectionHeaderItem(
            titleRes = Strings.map_section_name_hostels
        )
        FilterTab.OTHERS -> SectionHeaderItem(
            titleRes = Strings.map_section_name_others
        )
        FilterTab.STRUCTURES -> SectionHeaderItem(
            titleRes = Strings.map_section_name_structures
        )
    }

    private fun createListWithSections(markers: List<MapMarker>): List<Any> {
        val setOfTags = markers.map { it.tag }.toSet()
        val list = mutableListOf<Any>()
        for (tag in setOfTags) {
            if (tag.isNullOrEmpty()) continue
            list.add(SpaceItem.VERTICAL_24)
            list.add(SectionHeaderItem(title = tag))
            list.add(SpaceItem.VERTICAL_12)
            list.addAll(markers.filter { it.tag == tag })
        }
        val otherPlaces = markers.filter { it.tag.isNullOrEmpty() }
        if (otherPlaces.isNotEmpty()) {
            list.add(SpaceItem.VERTICAL_24)
            list.add(SectionHeaderItem(titleRes = Strings.map_section_tag_others))
            list.add(SpaceItem.VERTICAL_12)
            list.addAll(otherPlaces)
        }
        return list
    }
}
