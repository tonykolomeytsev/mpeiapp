package com.example.map.view


import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.map.MapFragmentPresenter
import com.example.map.R
import com.example.map.view.MapFragmentView.Companion.PAGE_BUILDINGS
import com.example.map.view.MapFragmentView.Companion.PAGE_FOODS
import com.example.map.view.MapFragmentView.Companion.PAGE_HOSTELS
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.SupportMapFragment
import dagger.android.support.DaggerFragment
import kekmech.ru.core.dto.Building
import kekmech.ru.coreui.Resources
import kotlinx.android.synthetic.main.fragment_map.*
import java.lang.Exception
import javax.inject.Inject


class MapFragment : DaggerFragment(), MapFragmentView {

    @Inject
    lateinit var presenter: MapFragmentPresenter

    override val contentView: ViewGroup get() = coordinator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(presenter)
        placeContentUnderStatusBar()
        viewPagerUI?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val tabs = listOf(tabBuildings, tabHostels, tabFoods)
                tabs.filterNotNull().forEachIndexed { index, view -> redrawTabItem(view, position == index) }
            }
        })
        tabBuildings?.setOnClickListener { scrollUiTo(PAGE_BUILDINGS) }
        tabHostels?.setOnClickListener { scrollUiTo(PAGE_HOSTELS) }
        tabFoods?.setOnClickListener { scrollUiTo(PAGE_FOODS) }
    }

    fun scrollUiTo(position: Int) {
        viewPagerUI?.setCurrentItem(position, true)
    }

    override fun placeContentUnderStatusBar() {
        val rectangle = Rect(0, 0, 0, 0)
        val window = activity?.window
        window?.decorView?.getWindowVisibleDisplayFrame(rectangle)
        val statusBarHeight = rectangle.top
        val p = mapView?.layoutParams as ViewGroup.MarginLayoutParams?
        p?.topMargin = -statusBarHeight
        mapView?.layoutParams = p
    }

    override fun onResume() {
        mapView?.onResume()
        super.onResume()
        presenter.onResume(this)
        placeContentUnderStatusBar()
    }

    override fun onPause() {
        mapView?.onPause()
        presenter.onPause(this)
        super.onPause()
    }

    override fun onLowMemory() {
        mapView?.onLowMemory()
        super.onLowMemory()
    }

    override fun onDestroy() {
        mapView?.onDestroy()
        super.onDestroy()
    }

    override fun setAdapter(mapUIAdapter: RecyclerView.Adapter<*>) {
        viewPagerUI?.adapter = mapUIAdapter
    }

    private fun redrawTabItem(item: View, selected: Boolean) {
        if (selected) {
            item.setBackgroundResource(R.drawable.map_tab_selected)
            item.alpha = 1f
        } else {
            item.setBackgroundColor(Color.TRANSPARENT)
            item.alpha = 0.75f
        }
        item.invalidate()
        Log.d("MApFragment", "redraw")
    }
}
