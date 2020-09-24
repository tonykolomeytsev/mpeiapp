package kekmech.ru.map

import com.example.map.R
import kekmech.ru.coreui.items.PullItem
import kekmech.ru.coreui.items.SectionHeaderItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.domain_map.dto.MarkerType
import kekmech.ru.map.items.TabBarItem
import kekmech.ru.map.presentation.FilterTab
import kekmech.ru.map.presentation.MapState

class MapListConverter {

    fun map(state: MapState): List<Any> {

        return mutableListOf<Any>().apply {
            add(PullItem)
            add(SpaceItem(0, 4))
            add(TabBarItem)

            createSectionHeader(state.selectedTab).let {
                add(SpaceItem.VERTICAL_24)
                add(it)
                add(SpaceItem.VERTICAL_12)
            }

            addAll(
                state.markers.filter { it.type == state.selectedTab.toMarkerType() }
            )
        }
    }

    private fun createSectionHeader(selectedTab: FilterTab): Any = when (selectedTab) {
        FilterTab.FOOD -> SectionHeaderItem(
            titleRes = R.string.map_section_name_eat,
            itemId = selectedTab.ordinal
        )
        FilterTab.BUILDINGS -> SectionHeaderItem(
            titleRes = R.string.map_section_name_buildings,
            itemId = selectedTab.ordinal
        )
        FilterTab.HOSTELS -> SectionHeaderItem(
            titleRes = R.string.map_section_name_hostels,
            itemId = selectedTab.ordinal
        )
        FilterTab.OTHERS -> SectionHeaderItem(
            titleRes = R.string.map_section_name_others,
            itemId = selectedTab.ordinal
        )
        FilterTab.STRUCTURES -> SectionHeaderItem(
            titleRes = R.string.map_section_name_structures,
            itemId = selectedTab.ordinal
        )
    }

    private fun FilterTab.toMarkerType() = when (this) {
        FilterTab.FOOD -> MarkerType.FOOD
        FilterTab.BUILDINGS -> MarkerType.BUILDING
        FilterTab.HOSTELS -> MarkerType.HOSTEL
        FilterTab.OTHERS -> MarkerType.OTHER
        FilterTab.STRUCTURES -> MarkerType.STRUCTURE
    }
}