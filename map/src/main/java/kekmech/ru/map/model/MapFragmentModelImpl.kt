package kekmech.ru.map.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.Marker
import kekmech.ru.core.dto.Building
import kekmech.ru.core.dto.Food
import kekmech.ru.core.dto.Hostel
import kekmech.ru.core.usecases.*
import javax.inject.Inject

class MapFragmentModelImpl @Inject constructor(
    private val getBuildingsUseCase: GetBuildingsUseCase,
    private val getHostelsUseCase: GetHostelsUseCase,
    private val getFoodsUseCase: GetFoodsUseCase,
    private val getMapStateUseCase: GetMapStateUseCase,
    private val setMapStateUseCase: SetMapStateUseCase
) : MapFragmentModel {
    override val buildings: LiveData<List<Building>>
        get() = getBuildingsUseCase()
    override val hostels: LiveData<List<Hostel>>
        get() = getHostelsUseCase()
    override val foods: LiveData<List<Food>>
        get() = getFoodsUseCase()

    override var state: Int
        get() = getMapStateUseCase()
        set(value) { setMapStateUseCase(value) }

    override val markers = mutableListOf<Marker>()

    override val selectedPlace = MutableLiveData<Any>()

    override fun selectPlace(any: Any?) {
        selectedPlace.value = any
    }
}