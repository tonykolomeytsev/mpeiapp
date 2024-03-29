package kekmech.ru.feature_map_api.domain.model

import java.io.Serializable

data class MapMarker(
    val uid: String,
    val address: String,
    val location: Location,
    val name: String,
    val type: MarkerType,
    val icon: String?,
    val tag: String?,
) : Serializable

data class Location(val lat: Double, val lng: Double) : Serializable

enum class MarkerType : Serializable {
    FOOD,
    BUILDING,
    HOSTEL,
    OTHER,
    STRUCTURE,

    UNDEFINED
}
