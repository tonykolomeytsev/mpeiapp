package kekmech.ru.core.repositories

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.Building
import kekmech.ru.core.dto.Food
import kekmech.ru.core.dto.Hostel

interface PlacesRepository {
    val buildings: LiveData<List<Building>>
    val foods: LiveData<List<Food>>
    val hostels: LiveData<List<Hostel>>

    companion object {
        const val COLLECTION_PLACES = "places"
        const val FIELD_TYPE = "type"
        const val TYPE_BUILDING = "building"
        const val TYPE_FOOD = "food"
        const val TYPE_HOSTEL = "hostel"

    }
}