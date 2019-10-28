package kekmech.ru.map.view


import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kekmech.ru.map.MapFragmentPresenter
import com.example.map.R
import kekmech.ru.map.model.MapFragmentModel.Companion.PAGE_BUILDINGS
import kekmech.ru.map.model.MapFragmentModel.Companion.PAGE_FOODS
import kekmech.ru.map.model.MapFragmentModel.Companion.PAGE_HOSTELS
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_map.*
import javax.inject.Inject


class MapFragment : DaggerFragment(), MapFragmentView {

    @Inject
    lateinit var presenter: MapFragmentPresenter

    override val contentView: ViewGroup get() = mapCoordinator

    override var onChangeStateListener: (Int) -> Unit = {}

    private var viewPagerUI: ViewPager2? = null

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
        tabBuildings?.setOnClickListener { scrollUiTo(PAGE_BUILDINGS) }
        tabHostels?.setOnClickListener { scrollUiTo(PAGE_HOSTELS) }
        tabFoods?.setOnClickListener { scrollUiTo(PAGE_FOODS) }
    }

    fun scrollUiTo(position: Int) {
        onChangeStateListener(position)
        viewPagerUI?.setCurrentItem(position, true)

    }

    override fun setState(state: Int) {
        viewPagerUI?.setCurrentItem(state, false)
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
        Log.d("MapFragment", "onResume")
        // dynamically load viewPager2
        viewPagerUI = ViewPager2(requireContext())
        viewPagerContainer?.removeAllViews()
        viewPagerContainer?.addView(viewPagerUI)
        viewPagerUI?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val tabs = listOf(tabBuildings, tabHostels, tabFoods)
                tabs.filterNotNull().forEachIndexed { index, view -> redrawTabItem(view, position == index) }
                onChangeStateListener(position)
            }
        })

        mapView?.onResume()
        super.onResume()
        presenter.onResume(this)
        placeContentUnderStatusBar()

    }

    override fun onPause() {
        Log.d("MapFragment", "onPause")
        viewPagerContainer?.removeAllViews()
        viewPagerUI = null
        System.gc()
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
