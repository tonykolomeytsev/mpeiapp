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
import kekmech.ru.core.repositories.PlacesRepository.Companion.TYPE_FOOD
import kekmech.ru.core.repositories.PlacesRepository.Companion.TYPE_HOSTEL
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(): PlacesRepository {
    private val mutableBuildings = MutableLiveData<List<Building>>()
    override val buildings: LiveData<List<Building>>
        get() {
            updateBuildings()
            return mutableBuildings
        }
    private val mutableFoods = MutableLiveData<List<Food>>()
    override val foods: LiveData<List<Food>>
        get() {
            updateFoods()
            return mutableFoods
        }
    private val mutableHostels = MutableLiveData<List<Hostel>>()
    override val hostels: LiveData<List<Hostel>>
        get() {
            updateHostels()
            return mutableHostels
        }

    private val buildingsRef = FirebaseFirestore.getInstance()
        .collection(COLLECTION_PLACES)
        .whereEqualTo(FIELD_TYPE, TYPE_BUILDING)
    private val hostelsRef = FirebaseFirestore.getInstance()
        .collection(COLLECTION_PLACES)
        .whereEqualTo(FIELD_TYPE, TYPE_HOSTEL)
    private val foodsRef = FirebaseFirestore.getInstance()
        .collection(COLLECTION_PLACES)
        .whereEqualTo(FIELD_TYPE, TYPE_FOOD)

    private fun updateBuildings() {
        buildingsRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mutableBuildings.value = task.result?.documents
                    ?.mapNotNull { it.toObject(Building::class.java) }
            } else {
                Log.e("PlacesRepository", task.exception.toString())
            }
        }
    }

    private fun updateHostels() {
        hostelsRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mutableHostels.value = task.result?.documents
                    ?.mapNotNull { it.toObject(Hostel::class.java) }
            } else {
                Log.e("PlacesRepository", task.exception.toString())
            }
        }
    }

    private fun updateFoods() {
        foodsRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mutableFoods.value = task.result?.documents
                    ?.mapNotNull { it.toObject(Food::class.java) }
            } else {
                Log.e("PlacesRepository", task.exception.toString())
            }
        }
    }
}