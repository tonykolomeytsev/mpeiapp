package kekmech.ru.map

import android.content.Context
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.map.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.GeoPoint
import kekmech.ru.core.dto.Building
import kekmech.ru.core.dto.Food
import kekmech.ru.core.dto.Hostel
import kekmech.ru.core.zipNullable
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.map.MapViewModel.MarkersFilter.*
import kekmech.ru.map.model.MapFragmentModel
import kekmech.ru.map.view.CustomMarkerView
import kekmech.ru.map.view.pages.*

class MapViewModel constructor(
    private val model: MapFragmentModel,
    private val context: Context
) : ViewModel(), OnMapReadyCallback {
    private var map = MutableLiveData<GoogleMap>()
    private var mapCustomizer = MapCustomizer(context)
    var mapUIAdapter = BaseAdapter.Builder()
        .registerViewTypeFactory(BuildingsItem.Factory())
        .registerViewTypeFactory(HostelsItem.Factory())
        .registerViewTypeFactory(FoodsItem.Factory())
        .build()
        .apply { baseItems.addAll(listOf(BuildingsItem(model), HostelsItem(model), FoodsItem(model))) }
    var currentMapFilter = MutableLiveData<MarkersFilter>().apply { value = BUILDINGS }
    var currentMapMarkers: LiveData<List<Marker>> = Transformations.map(
        zipNullable(currentMapFilter, model.buildings, model.hostels, model.foods, map)) { (filter, buildings, hostels, foods, map) ->
        if (map == null) return@map emptyList<Marker>()
        val newMarkers = when (filter ?: BUILDINGS) {
            BUILDINGS -> buildings?.let { it -> placeBuildingMarkers(map, it) }.orEmpty()
            HOSTELS -> hostels?.let { it -> placeHostelMarkers(map, it) }.orEmpty()
            FOODS -> foods?.let { it -> placeFoodMarkers(map, it) }.orEmpty()
        }
        model.markers.forEach { it.remove() }
        model.markers = newMarkers
        return@map newMarkers
    }

    init {
        model.selectedPlaceListener = { place ->
            map.value?.let { map ->
                selectPlace(place, map)
            }
        }

    }

    override fun onMapReady(map: GoogleMap?) {
        mapCustomizer.initMap(map)
        this.map.value = map
    }

    private fun selectPlace(place: Any, map: GoogleMap) {
        try {
            when (place) {
                is SingleBuildingItem -> {
                    model.markers.find { it.title == place.building.name }?.apply {
                        showInfoWindow()
                        mapCustomizer.animateCameraTo(map, this)
                    }
                }
                is SingleHostelItem -> {
                    model.markers.find { it.title == place.hostel.name }?.apply {
                        showInfoWindow()
                        mapCustomizer.animateCameraTo(map, this)
                    }
                }
                is SingleFoodItem -> {
                    model.markers.find { it.title == place.food.name }?.apply {
                        showInfoWindow()
                        mapCustomizer.animateCameraTo(map, this)
                    }
                }
            }
        } catch (e: Exception) { e.printStackTrace() }
    }

    private val GeoPoint.latLng get() = LatLng(this.latitude, this.longitude)

    private fun placeBuildingMarkers(map: GoogleMap, places: List<Building>): List<Marker> {
        return places.map { place ->
            map.addMarker(
                MarkerOptions()
                    .position(place.location.latLng)
                    .title(place.name)
                    .icon(CustomMarkerView(context, R.layout.item_placemark_building) {
                        (it.findViewById<TextView>(R.id.textViewPlacemarkTitle)).text = place.letter
                    }.getMarkerIcon())
            )
        }
    }

    private fun placeFoodMarkers(map: GoogleMap, places: List<Food>): List<Marker> {
        return places.map { food ->
            map.addMarker(
                MarkerOptions()
                    .position(food.location.latLng)
                    .title(food.name)
                    .icon(CustomMarkerView(context, R.layout.item_placemark_food) {}.getMarkerIcon())
            )
        }
    }

    private fun placeHostelMarkers(map: GoogleMap, places: List<Hostel>): List<Marker> {
        return places.map { hostel ->
            map.addMarker(
                MarkerOptions()
                    .position(hostel.location.latLng)
                    .title(hostel.name)
                    .icon(CustomMarkerView(context, R.layout.item_placemark_hostel) {}.getMarkerIcon())
            )
        }
    }

    fun changeMapFilterTo(mapFilter: MarkersFilter) { currentMapFilter.value = mapFilter }

    enum class MarkersFilter { BUILDINGS, FOODS, HOSTELS }
}

