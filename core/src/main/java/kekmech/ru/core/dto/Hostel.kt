package kekmech.ru.core.dto

import android.location.Location
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
data class Hostel(
    @PropertyName("name")
    val name: String,
    @PropertyName("location")
    val location: Location
)