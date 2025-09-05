package kekmech.ru.domain_map.dto

public data class MapMarker(
    val uid: String,
    val address: String,
    val location: Location,
    val name: String,
    val type: MarkerType,
    val icon: String?,
    val tag: String?,
)

public data class Location(val lat: Double, val lng: Double)

public enum class MarkerType {
    FOOD,
    BUILDING,
    HOSTEL,
    OTHER,
    STRUCTURE,

    UNDEFINED
}
