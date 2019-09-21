package com.example.map.view


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.map.MapFragmentPresenter
import com.example.map.R
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import dagger.android.support.DaggerFragment
import kekmech.ru.coreui.Resources
import javax.inject.Inject

class MapFragment : DaggerFragment(), MapFragmentView, OnMapReadyCallback {

    @Inject
    lateinit var presenter: MapFragmentPresenter

    lateinit var mapView: MapView
    lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        try {
            MapsInitializer.initialize(this.activity)
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }

        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_map, container, false)

        // Gets the MapView from the XML layout and creates it
        mapView = v.findViewById(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)

        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(this)

        return v
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.apply {
            uiSettings.isMyLocationButtonEnabled = false
            isMyLocationEnabled = false


            // Updates the location and zoom of the MapView
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(43.1, -87.9), 10f)
            animateCamera(cameraUpdate)
        }
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
        activity?.window?.statusBarColor = Resources.getColor(context, android.R.color.transparent)
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


}
