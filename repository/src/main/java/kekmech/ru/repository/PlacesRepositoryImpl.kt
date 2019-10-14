package kekmech.ru.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import kekmech.ru.core.dto.Building
import kekmech.ru.core.dto.Food
import kekmech.ru.core.dto.Hostel
import kekmech.ru.core.repositories.PlacesRepository
import kekmech.ru.core.repositories.PlacesRepository.Companion.COLLECTION_PLACES
import kekmech.ru.core.repositories.PlacesRepository.Companion.FIELD_TYPE
import kekmech.ru.core.repositories.PlacesRepository.Companion.TYPE_BUILDING
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(): PlacesRepository {
    private val mutableBuildings = MutableLiveData<List<Building>>()
    override val buildings: LiveData<List<Building>>
        get() {
            updateBuildings()
            return mutableBuildings
        }
    override val foods: LiveData<List<Food>> = MutableLiveData()
    override val hostels: LiveData<List<Hostel>> = MutableLiveData()

    private val buildingsRef = FirebaseFirestore.getInstance()
        .collection(COLLECTION_PLACES)
        .whereEqualTo(FIELD_TYPE, TYPE_BUILDING)

    private fun updateBuildings() {
        buildingsRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.documents?.forEach {
                    Log.d("PlacesRepository", it.data.toString())
                }
            } else {
                Log.e("PlacesRepository", task.exception.toString())
            }
        }
    }
}