package kekmech.ru.map.view


import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.example.map.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import kekmech.ru.map.MapViewModel
import kekmech.ru.map.MapViewModel.MarkersFilter.*
import kekmech.ru.map.model.MapFragmentModel.Companion.PAGE_BUILDINGS
import kekmech.ru.map.model.MapFragmentModel.Companion.PAGE_FOODS
import kekmech.ru.map.model.MapFragmentModel.Companion.PAGE_HOSTELS
import kotlinx.android.synthetic.main.fragment_map.*
import org.koin.android.ext.android.inject


class MapFragment : Fragment(R.layout.fragment_map) {

    private val viewModel: MapViewModel by inject()
    private var viewPagerUI: ViewPager2? = null

    override fun onResume() {
        super.onResume()
        // dynamically load viewPager2 (coz viewpager2 issue)
        viewPagerUI = ViewPager2(requireContext())
        viewPagerContainer?.removeAllViews()
        viewPagerContainer?.addView(viewPagerUI)
        viewPagerUI?.registerOnPageChangeCallback(viewPagerOnPageChangeCallback)
        viewPagerUI?.adapter = viewModel.mapUIAdapter

        viewModel.currentMapFilter.observe(this, Observer {
            val directPage = when (it ?: BUILDINGS) {
                BUILDINGS -> (PAGE_BUILDINGS)
                HOSTELS -> (PAGE_HOSTELS)
                FOODS -> (PAGE_FOODS)
            }
            if (directPage != viewPagerUI?.currentItem) scrollUiTo(directPage)
        })
        viewModel.currentMapMarkers.observe(this, Observer {
            Log.d("MapFragment", "Map markers changed")
        })

        tabBuildings?.setOnClickListener { viewModel.changeMapFilterTo(BUILDINGS) }
        tabHostels?.setOnClickListener { viewModel.changeMapFilterTo(HOSTELS) }
        tabFoods?.setOnClickListener { viewModel.changeMapFilterTo(FOODS) }

        mapView?.onResume()
        setupBottomMenu()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setOnApplyWindowInsetsListener { _, insets ->
            val statusBarSize = insets.systemWindowInsetTop
            val p = mapView?.layoutParams as ViewGroup.MarginLayoutParams?
            p?.topMargin = -statusBarSize
            mapView?.layoutParams = p
            mapView?.setPadding(0, statusBarSize, 0, 0)
            insets
        }
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(viewModel)
    }

    private val viewPagerOnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            val tabs = listOf(tabBuildings, tabHostels, tabFoods)
            tabs.filterNotNull().forEachIndexed { index, view -> redrawTabItem(view, position == index) }
            viewModel.changeMapFilterTo(when(position) {
                PAGE_BUILDINGS -> BUILDINGS
                PAGE_HOSTELS -> HOSTELS
                else -> FOODS
            })
        }
    }

    private fun scrollUiTo(position: Int) = viewPagerUI?.setCurrentItem(position, true)

    private fun setupBottomMenu() {
        val behavior = BottomSheetBehavior.from(bottomMenu)
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(v: View, p: Float) {
                buttonExpand?.rotation = 180f * p
            }

            override fun onStateChanged(v: View, s: Int) {
                if (s == STATE_COLLAPSED) buttonExpand?.rotation = 0f
                if (s == STATE_EXPANDED) buttonExpand?.rotation = 180f
            }
        })
        buttonExpand?.rotation = if (behavior.state == STATE_COLLAPSED) 0f else 180f
        buttonExpand?.setOnClickListener {
            behavior.state = if (behavior.state == STATE_COLLAPSED) STATE_EXPANDED else STATE_COLLAPSED
        }
        bottomMenu?.setOnApplyWindowInsetsListener { v, insets ->
            v.updatePadding(0, 0, 0, 0)
            insets
        } // костыль
    }

    override fun onPause() {
        super.onPause()
        viewPagerContainer?.removeAllViews()
        viewPagerUI = null
        System.gc()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        mapView?.onLowMemory()
        super.onLowMemory()
    }

    override fun onDestroy() {
        mapView?.onDestroy()
        super.onDestroy()
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
