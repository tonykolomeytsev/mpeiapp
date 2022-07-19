package kekmech.ru.map.ext

import kekmech.ru.domain_map.dto.MarkerType
import kekmech.ru.map.elm.FilterTab

internal fun FilterTab.toMarkerType() = when (this) {
    FilterTab.FOOD -> MarkerType.FOOD
    FilterTab.BUILDINGS -> MarkerType.BUILDING
    FilterTab.HOSTELS -> MarkerType.HOSTEL
    FilterTab.OTHERS -> MarkerType.OTHER
    FilterTab.STRUCTURES -> MarkerType.STRUCTURE
}

internal fun MarkerType.toFilterTab() = when (this) {
    MarkerType.FOOD -> FilterTab.FOOD
    MarkerType.BUILDING -> FilterTab.BUILDINGS
    MarkerType.HOSTEL -> FilterTab.HOSTELS
    MarkerType.OTHER -> FilterTab.OTHERS
    MarkerType.STRUCTURE -> FilterTab.STRUCTURES
    MarkerType.UNDEFINED -> null
}