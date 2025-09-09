package kekmech.ru.feature_map_api.domain.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
public data class MapMarker(
    val uid: String,
    val address: String,
    val location: Location,
    val name: String,
    val type: MarkerType,
    val icon: String?,
    val tag: String?,
) : Serializable

@Keep
public data class Location(val lat: Double, val lng: Double) : Serializable

@Keep
public enum class MarkerType : Serializable {
    FOOD,
    BUILDING,
    HOSTEL,
    OTHER,
    STRUCTURE,

    UNDEFINED
}
