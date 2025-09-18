package kekmech.ru.feature_map_impl.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MapMarkerDto(
    @SerialName("uid")
    val uid: String,

    @SerialName("address")
    val address: String,

    @SerialName("location")
    val location: LocationDto,

    @SerialName("name")
    val name: String,

    @SerialName("type")
    val type: MarkerTypeDto,

    @SerialName("icon")
    val icon: String?,

    @SerialName("tag")
    val tag: String?,
)

@Serializable
internal data class LocationDto(
    @SerialName("lat")
    val lat: Double,

    @SerialName("lng")
    val lng: Double,
)

@Serializable
internal enum class MarkerTypeDto {
    @SerialName("FOOD")
    FOOD,

    @SerialName("BUILDING")
    BUILDING,

    @SerialName("HOSTEL")
    HOSTEL,

    @SerialName("OTHER")
    OTHER,

    @SerialName("STRUCTURE")
    STRUCTURE,

    @SerialName("UNDEFINED")
    UNDEFINED
}
