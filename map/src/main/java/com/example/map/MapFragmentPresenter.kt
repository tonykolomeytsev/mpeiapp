package com.example.map

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.map.model.MapFragmentModel
import com.example.map.view.MapFragmentView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.GeoPoint
import kekmech.ru.core.Presenter
import kekmech.ru.core.dto.Building
import javax.inject.Inject
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import kekmech.ru.coreui.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MapFragmentPresenter @Inject constructor(
    private val model: MapFragmentModel,
    private val context: Context
) : Presenter<MapFragmentView>(), OnMapReadyCallback {
    private var map: GoogleMap? = null
    private var view: MapFragmentView? = null
    private var markers = mutableListOf<Marker>()

    override fun onMapReady(map: GoogleMap?) {
        Log.d("MapFragmentPresenter", map.toString())
        this.map = map
        map?.apply {
            setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.google_map_style))
            isMyLocationEnabled = false
            isBuildingsEnabled = true
            isTrafficEnabled = false
            isIndoorEnabled = true
            setPadding(0,0,0,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 162f, context.resources.displayMetrics).toInt())
            mapType = GoogleMap.MAP_TYPE_NORMAL
            uiSettings.apply {
                isCompassEnabled = false
                isMyLocationButtonEnabled = false
                isZoomControlsEnabled = false
                isRotateGesturesEnabled = false
            }
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(55.755060, 37.708431), 17f))
        }
        view?.placeContentUnderStatusBar()
    }

    override fun onResume(view: MapFragmentView) {
        super.onResume(view)
        model.buildings.observe(view, Observer {
            view.setBuildings(it)
            view.onBuildingSelected = {index ->
                view.setBuildingDescription(it[index].name)
                selectBuilding(it, index)
            }
            view.setBuildingDescription(it[0].name)
            placeMarkers(it)
        })
        // TODO сделать отображение общаг и столовок
        this.view = view
    }

    private fun placeMarkers(it: List<Building>) {
        map?.apply {
            markers.forEach { it.remove() }
            markers.clear()
            lastSelectedBuilding = -1

            it.forEach {place ->
                val marker = LayoutInflater
                    .from(context)
                    .inflate(R.layout.item_placemark_building, FrameLayout(context), false)
                (marker.findViewById<TextView>(R.id.textViewPlacemarkTitle)).text = place.letter

                if (view != null) markers.add(addMarker(
                     MarkerOptions()
                        .position(place.location.toLatLng())
                        .title(place.name)
                        .icon(getMarkerIcon(view!!.contentView, place.letter, false))
                ))
            }
        }
    }

    override fun onPause(view: MapFragmentView) {
        super.onPause(view)
        this.view = null
    }


    private var lastSelectedBuilding = -1
    private fun selectBuilding(buildings: List<Building>, i: Int) {
        val building = buildings[i]
        map?.apply {
            animateCamera(CameraUpdateFactory.newLatLngZoom(building.location.toLatLng(), 17f), 200, null)
        }

        val prevBuilding = buildings.getOrNull(lastSelectedBuilding)
        markers.getOrNull(i)
            ?.setIcon(getMarkerIcon(view!!.contentView, building.letter, true))

        if (lastSelectedBuilding != i) {
            if (prevBuilding != null) markers.getOrNull(lastSelectedBuilding)
                ?.setIcon(getMarkerIcon(view!!.contentView, prevBuilding.letter, false))
            lastSelectedBuilding = i
        }
    }

    private fun GeoPoint.toLatLng(): LatLng = LatLng(this.latitude, this.longitude)

    fun getMarkerIcon(root: ViewGroup, text: String?, isSelected: Boolean): BitmapDescriptor? {
        val markerView = CustomMarkerView(root, text, isSelected)
        markerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        markerView.layout(0, 0, markerView.measuredWidth, markerView.measuredHeight)
        markerView.isDrawingCacheEnabled = true
        markerView.invalidate()
        markerView.buildDrawingCache(false)
        return BitmapDescriptorFactory.fromBitmap(markerView.drawingCache)
    }

    private class CustomMarkerView(root: ViewGroup, text: String?, isSelected: Boolean) : FrameLayout(root.context) {
        private var mTitle: TextView

        init {
            View.inflate(context, R.layout.item_placemark_building, this)
            mTitle = findViewById(R.id.textViewPlacemarkTitle)
            measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
            mTitle.text = text
            if (isSelected) {
                mTitle.backgroundTintList = ColorStateList.valueOf(Resources.getColor(context, R.color.colorPrimary))
                mTitle.setTextColor(Resources.getColor(context, R.color.colorWhite))
            } else {
                mTitle.backgroundTintList = ColorStateList.valueOf(Resources.getColor(context, R.color.colorBlackTransparent))
                mTitle.setTextColor(Resources.getColor(context, R.color.colorWhite))
            }
        }


    }
}

