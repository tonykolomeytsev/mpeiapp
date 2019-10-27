package kekmech.ru.map

import android.content.Context
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.map.R
import kekmech.ru.map.model.MapFragmentModel
import kekmech.ru.map.model.MapFragmentModel.Companion.PAGE_BUILDINGS
import kekmech.ru.map.model.MapFragmentModel.Companion.PAGE_FOODS
import kekmech.ru.map.model.MapFragmentModel.Companion.PAGE_HOSTELS
import kekmech.ru.map.view.MapFragmentView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.GeoPoint
import kekmech.ru.core.Presenter
import kekmech.ru.core.dto.Building
import kekmech.ru.core.dto.Food
import kekmech.ru.core.dto.Hostel
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.map.view.CustomMarkerView
import kekmech.ru.map.view.pages.*
import javax.inject.Inject

class MapFragmentPresenter @Inject constructor(
    private val model: MapFragmentModel,
    private val context: Context
) : Presenter<MapFragmentView>(), OnMapReadyCallback {
    private var map: GoogleMap? = null
    private var view: MapFragmentView? = null
    private var mapCustomizer = MapCustomizer(context)
    private var mapUIAdapter = BaseAdapter.Builder()
        .registerViewTypeFactory(BuildingsItem.Factory())
        .registerViewTypeFactory(HostelsItem.Factory())
        .registerViewTypeFactory(FoodsItem.Factory())
        .build()
        .apply { baseItems.addAll(listOf(BuildingsItem(model), HostelsItem(model), FoodsItem(model))) }

    override fun onMapReady(map: GoogleMap?) {
        Log.d("MapFragmentPresenter", map.toString())
        this.map = map
        mapCustomizer.initMap(map)
        view?.placeContentUnderStatusBar()
        if (model.markers.isEmpty() && view != null) replaceMarkers(view!!)
    }

    override fun onResume(view: MapFragmentView) {
        super.onResume(view)
        this.view = view
        //replaceMarkers(view)
        view.setAdapter(mapUIAdapter)
        view.setState(model.state)
        view.onChangeStateListener = {
            if (model.state != it) {
                model.state = it
                replaceMarkers(view)
            }
        }
        model.selectedPlaceListener = { place ->
            Log.d("MapPresenter", "selectedPlace $place")
            try {
                val markers = model.markers
                when (place) {
                    is SingleBuildingItem -> {
                        model.markers.find { it.title == place.building.name }?.apply {
                            Log.d("MapPresenter", "selectedMarker ${this.title}")
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
    }

    override fun onPause(view: MapFragmentView) {
        super.onPause(view)
        this.view = null
    }

    val GeoPoint.latLng get() = LatLng(this.latitude, this.longitude)


    private fun placeBuildingMarkers(places: List<Building>) {
        map?.apply {
            model.markers.forEach { it.remove() }
            model.markers.clear()

            places.forEachIndexed { i, place ->
                if (view != null) model.markers.add(addMarker(
                     MarkerOptions()
                        .position(place.location.latLng)
                        .title(place.name)
                        .icon(CustomMarkerView(context, R.layout.item_placemark_building) {
                            (it.findViewById<TextView>(R.id.textViewPlacemarkTitle)).text = place.letter
                        }.getMarkerIcon())
                ))
            }
        }
    }

    private fun placeFoodMarkers(places: List<Food>) {
        map?.apply {
            model.markers.forEach { it.remove() }
            model.markers.clear()

            places.forEachIndexed { i, food ->
                if (view != null) model.markers.add(addMarker(
                    MarkerOptions()
                        .position(food.location.latLng)
                        .title(food.name)
                        .icon(CustomMarkerView(context, R.layout.item_placemark_food) {}.getMarkerIcon())
                ))
            }
        }
    }

    private fun placeHostelMarkers(places: List<Hostel>) {
        map?.apply {
            model.markers.forEach { it.remove() }
            model.markers.clear()

            places.forEachIndexed { i, hostel ->
                if (view != null) model.markers.add(addMarker(
                    MarkerOptions()
                        .position(hostel.location.latLng)
                        .title(hostel.name)
                        .icon(CustomMarkerView(context, R.layout.item_placemark_hostel) {}.getMarkerIcon())
                ))
            }
        }
    }

    private fun replaceMarkers(view: MapFragmentView) {
        when (model.state) {
            PAGE_BUILDINGS -> model.buildings.observe(view, Observer { placeBuildingMarkers(it) })
            PAGE_FOODS -> model.foods.observe(view, Observer { placeFoodMarkers(it) })
            PAGE_HOSTELS -> model.hostels.observe(view, Observer { placeHostelMarkers(it) })
            else -> {
                model.markers.forEach { it.remove() }
                model.markers.clear()
            }
        }
    }
}

