package com.example.map

import android.app.ActionBar
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
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
                selectBuilding(it[index])
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

            it.forEach {place ->
                val marker = LayoutInflater
                    .from(context)
                    .inflate(R.layout.item_placemark_building, FrameLayout(context), false)
                (marker.findViewById<TextView>(R.id.textViewPlacemarkTitle)).text = place.letter

                markers.add(addMarker(
                    MarkerOptions()
                        .position(place.location.toLatLng())
                        .title(place.name)
                        .icon(createMarker(context, place.letter))
                ))
            }
        }
    }

    fun createMarker(context: Context, text: String): BitmapDescriptor {
        val marker = MarkerOptions()
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32f, context.resources.displayMetrics).toInt()
        val markerView =
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.item_placemark_building,
                null
            )
        markerView.layoutParams = ViewGroup.LayoutParams(
            px,
            px
        )
        markerView.layout(0, 0, px, px)
        markerView.buildDrawingCache()
        val bedNumberTextView = markerView.findViewById<TextView>(R.id.textViewPlacemarkTitle)

        val mDotMarkerBitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mDotMarkerBitmap)
        bedNumberTextView.text = text
        bedNumberTextView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        bedNumberTextView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        bedNumberTextView.layout(0,0,bedNumberTextView.measuredWidth,bedNumberTextView.measuredHeight)
        bedNumberTextView.buildDrawingCache()
        markerView.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(mDotMarkerBitmap)
    }

    override fun onPause(view: MapFragmentView) {
        super.onPause(view)
        this.view = null
    }

    private fun selectBuilding(building: Building) {
        map?.apply {
            animateCamera(CameraUpdateFactory.newLatLngZoom(building.location.toLatLng(), 17f), 200, null)
        }
    }

    private fun GeoPoint.toLatLng(): LatLng = LatLng(this.latitude, this.longitude)
}

