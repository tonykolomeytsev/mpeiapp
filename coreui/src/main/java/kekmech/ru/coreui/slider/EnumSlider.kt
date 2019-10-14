package kekmech.ru.coreui.slider

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.coreui.R
import kekmech.ru.coreui.adapter.BaseAdapter

class EnumSlider(context: Context, attrs: AttributeSet?, defStyle: Int) : RecyclerView(context, attrs, defStyle) {
    private val baseAdapter = BaseAdapter.Builder()
        .registerViewTypeFactory(EnumSliderItem.Factory())
        .build()
    private val halfWidthOfItem = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, context.resources.displayMetrics).toInt()

    constructor(context: Context): this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    var items = listOf<String>()
        set(value) {
            field = value
            updateAdapter()
        }
    var selectedPosition: Int
        get() = (layoutManager as EnumSliderLayoutManager).selectedPosition
        set(value) { (layoutManager as EnumSliderLayoutManager).selectedPosition = value }

    init {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.EnumSlider, 0, 0)
        try {
            items// = a.getInteger(R.styleable.EnumSlider_itemsCount, 8)
        } finally {
            a.recycle()
        }
        clipToPadding = false
        post {
            setPadding(measuredWidth / 2 - halfWidthOfItem, 0, measuredWidth / 2 - halfWidthOfItem, 0)
            layoutManager = EnumSliderLayoutManager(context, measuredWidth, halfWidthOfItem*2)
            updateAdapter()
        }
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == SCROLL_STATE_IDLE) {
                    Log.d("ScrollState", "IDLE")
                    (layoutManager as EnumSliderLayoutManager).onTouchRelease(this@EnumSlider, null)
                }
            }
        })
    }

    private fun updateAdapter() {
        adapter = baseAdapter.apply {
            baseItems.clear()
            baseItems.addAll(items.map { EnumSliderItem(it) })
            notifyDataSetChanged()
        }
        viewTreeObserver.addOnGlobalLayoutListener(this::onItemsLayOut)
    }

    private fun onItemsLayOut() {
        Log.d("EnumSlider", "onItemsLayOut")
        (layoutManager as EnumSliderLayoutManager?)?.initFirstState()
        viewTreeObserver.removeOnGlobalLayoutListener(this::onItemsLayOut)
    }

}