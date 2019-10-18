package kekmech.ru.feed

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import kekmech.ru.core.Presenter
import kekmech.ru.coreui.Resources
import kekmech.ru.coreui.adapter.BaseAdapter
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_feed.view.*
import javax.inject.Inject
import androidx.core.widget.NestedScrollView
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView


class FeedFragment @Inject constructor() : DaggerFragment(), IFeedFragment {

    @Inject
    lateinit var presenter: Presenter<IFeedFragment>

    @Volatile
    private var lock = true

    override var requiredAction: String = ""

    init { retainInstance = true }

    /**
     * fires when user scroll his feed down to the end.
     * presenter must subscribe for this event and load new items to the feed
     */
    override var onEditListener: () -> Unit = {}

    override var bottomReachListener: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feed, container, false)
        view.recyclerView.layoutManager = LinearLayoutManager(activity)
        view.recyclerView.setRecycledViewPool(recycledViewPool)
        view.recyclerViewMenu.layoutManager = LinearLayoutManager(activity)
        view.nestedScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, _, _, _ ->
            if (view.nestedScroll.scrollY >= ((v.getChildAt(0).measuredHeight - v.measuredHeight) * 0.95)) {
                if (!lock) {
                    lock = true
                    bottomReachListener()
                }
            }
        })
        view.fab.setOnClickListener { it.postOnAnimation { onEditListener() } }
        return view
    }

    override fun onResume() {
        super.onResume()
        unlock()
        presenter.onResume(this)
        requiredAction = ""
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause(this)
    }

    override fun showEditDialog(dialog: AlertDialog) {
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    override fun setStatus(title: String, dayInfo: String, weekInfo: String) {
        main_collapsing.title = title
        toolbar.title = title
        textViewDayOfWeekInfo.text = dayInfo.substringBefore(',') + ", "
        textViewDayOfMonthInfo.text = dayInfo.substringAfter(',')
        textViewWeekInfo.text = weekInfo
    }

    override fun updateAdapterIfNull(adapter: BaseAdapter) {
        if (recyclerView.adapter == null) {
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    override fun unlock() {
        lock = false
    }

    override fun withinContext(listener: (context: Context) -> Unit) {
        listener(requireContext())
    }

    fun notifyActionRequired(action: String) {
        this.requiredAction = action
    }

    @Deprecated("Menu will be deleted")
    override fun showMenu() {
        cardViewMenu.visibility = View.VISIBLE
        val w = recyclerViewMenu.measuredWidth
        val h = recyclerViewMenu.measuredHeight
        val wSrc = cardViewMenu.measuredWidth
        val hSrc = cardViewMenu.measuredHeight
        val cornerRadius = cardViewMenu.radius
        val dp4 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics)
        val anim = ValueAnimator.ofFloat(0f, 1f)
        anim.addUpdateListener { valueAnimator ->
            val f = valueAnimator.animatedValue as Float
            val layoutParams = cardViewMenu.layoutParams
            layoutParams.width = (wSrc + (w - wSrc)*f).toInt()
            layoutParams.height = (hSrc + (h - hSrc)*f).toInt()
            cardViewMenu.layoutParams = layoutParams
            cardViewMenu.radius = dp4 + cornerRadius*(1f - f)
            cardViewMenu.requestLayout()
        }
        anim.duration = 100
        fab.animate()
            .alpha(0f)
            .setDuration(100)
            .withEndAction {
                anim.start()
                fab.isEnabled = false
            }
            .start()

        recyclerViewMenu.alpha = 0f
        recyclerViewMenu.animate()
            .alpha(1f)
            .setDuration(100)
            .setStartDelay(200)
            .start()

        frameLayoutMenuOverlay.visibility = View.VISIBLE
        frameLayoutMenuOverlay.setOnClickListener { hideMenu() }
    }

    @Deprecated("Menu will be deleted")
    override fun hideMenu() {
        fab.animate()
            .alpha(1f)
            .setDuration(100)
            .setStartDelay(100)
            .withEndAction {
                cardViewMenu.visibility = View.INVISIBLE
                fab.isEnabled = true
            }
            .start()

        recyclerViewMenu.animate()
            .alpha(0f)
            .setDuration(100)
            .start()

        val w = recyclerViewMenu.measuredWidth
        val h = recyclerViewMenu.measuredHeight
        val dp56 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56f, resources.displayMetrics)
        val dp4 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics)
        val dp28 = dp56/2
        val anim = ValueAnimator.ofFloat(1f, 0f)
        anim.addUpdateListener { valueAnimator ->
            val f = valueAnimator.animatedValue as Float
            val layoutParams = cardViewMenu.layoutParams
            layoutParams.width = (dp56 + (w - dp56)*f).toInt()
            layoutParams.height = (dp56 + (h - dp56)*f).toInt()
            cardViewMenu.layoutParams = layoutParams
            cardViewMenu.radius = dp4 + (dp28-dp4)*(1f-f)
            cardViewMenu.requestLayout()
        }
        anim.duration = 100
        anim.start()
        frameLayoutMenuOverlay.visibility = View.INVISIBLE
    }

    override fun updateMenu(menuAdapter: BaseAdapter) {
        recyclerViewMenu.adapter = menuAdapter
    }

    companion object {
        private val recycledViewPool = RecyclerView.RecycledViewPool()
    }
}
