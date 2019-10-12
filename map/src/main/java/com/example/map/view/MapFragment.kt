package com.example.map.view


import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.map.MapFragmentPresenter
import com.example.map.R
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.logo.Alignment
import com.yandex.mapkit.logo.HorizontalAlignment
import com.yandex.mapkit.logo.VerticalAlignment
import com.yandex.mapkit.map.CameraPosition
import dagger.android.support.DaggerFragment
import kekmech.ru.coreui.Resources
import kotlinx.android.synthetic.main.fragment_map.*
import javax.inject.Inject
import android.R.attr.top
import android.graphics.Rect
import android.widget.FrameLayout
import android.widget.TextView
import com.yandex.mapkit.map.PlacemarkAnimation
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider


class MapFragment : DaggerFragment(), MapFragmentView {

    @Inject
    lateinit var presenter: MapFragmentPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView?.map?.move(
            CameraPosition(Point(55.755060, 37.708431), 16.0f, 0.0f, 0.0f)
        )
        mapView?.map?.logo?.setAlignment(Alignment(HorizontalAlignment.RIGHT, VerticalAlignment.TOP))
        mapView?.map?.apply {
            mapObjects.addPlacemark(Point(55.755060, 37.708431), getVP())
        }
    }

    fun getVP(): ViewProvider {
        val v = LayoutInflater.from(context).inflate(R.layout.item_placemark_building, null)
        v.findViewById<TextView>(R.id.textViewPlacemarkTitle).text = "В"
        return ViewProvider(v)
    }

    private fun placeContentUnderStatusBar() {
        val rectangle = Rect(0, 0, 0, 0)
        val window = activity?.window
        window?.decorView?.getWindowVisibleDisplayFrame(rectangle)
        val statusBarHeight = rectangle.top
        val p = mapView.layoutParams as ViewGroup.MarginLayoutParams
        p.topMargin = -statusBarHeight
        mapView.layoutParams = p
    }

    override fun onResume() {
        super.onResume()
        placeContentUnderStatusBar()
        activity?.window?.statusBarColor = Resources.getColor(context, android.R.color.transparent)
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
        MapKitFactory.getInstance().onStop()
    }

}
