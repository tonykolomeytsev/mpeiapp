package kekmech.ru.map.model

import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.Marker
import kekmech.ru.core.dto.Building
import kekmech.ru.core.dto.Food
import kekmech.ru.core.dto.Hostel
import kekmech.ru.core.usecases.*

class MapFragmentModelImpl constructor(
    private val getBuildingsUseCase: GetBuildingsUseCase,
    private val getHostelsUseCase: GetHostelsUseCase,
    private val getFoodsUseCase: GetFoodsUseCase
) : MapFragmentModel {
    override val buildings: LiveData<List<Building>>
        get() = getBuildingsUseCase()
    override val hostels: LiveData<List<Hostel>>
        get() = getHostelsUseCase()
    override val foods: LiveData<List<Food>>
        get() = getFoodsUseCase()

    override var markers = listOf<Marker>()
    override var selectedPlaceListener: (Any) -> Unit = {}
}