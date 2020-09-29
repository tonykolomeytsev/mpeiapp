package kekmech.ru.map.ext

import kekmech.ru.domain_map.dto.MarkerType
import kekmech.ru.map.presentation.FilterTab

fun FilterTab.toMarkerType() = when (this) {
    FilterTab.FOOD -> MarkerType.FOOD
    FilterTab.BUILDINGS -> MarkerType.BUILDING
    FilterTab.HOSTELS -> MarkerType.HOSTEL
    FilterTab.OTHERS -> MarkerType.OTHER
    FilterTab.STRUCTURES -> MarkerType.STRUCTURE
}