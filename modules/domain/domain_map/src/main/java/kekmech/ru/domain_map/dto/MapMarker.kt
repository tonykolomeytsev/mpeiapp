package kekmech.ru.domain_map.dto

data class MapMarker(
    val uid: String,
    val address: String,
    val location: Location,
    val name: String,
    val type: MarkerType,
    val icon: String?,
    val tag: String?,
)

data class Location(val lat: Double, val lng: Double)

enum class MarkerType {
    FOOD,
    BUILDING,
    HOSTEL,
    OTHER,
    STRUCTURE,

    UNDEFINED
}