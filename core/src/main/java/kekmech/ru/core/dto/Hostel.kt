package kekmech.ru.core.dto

import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
data class Hostel(
    @PropertyName("name")
    var name: String = "",
    @PropertyName("location")
    var location: GeoPoint = GeoPoint(55.755060, 37.708431),
    @PropertyName("address")
    var address: String = ""
)