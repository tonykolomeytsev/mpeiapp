package kekmech.ru.feed

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
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
import android.support.v4.widget.NestedScrollView




class FeedFragment @Inject constructor() : DaggerFragment(), IFeedFragment {

    @Inject
    lateinit var presenter: Presenter<IFeedFragment>

    override val activityContext: Context? get() = activity

    @Volatile private var lock = true

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
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause(this)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity!!.window.statusBarColor = Resources.getColor(context, R.color.colorSecondary)
    }

    override fun showEditDialog(dialog: AlertDialog) {
        dialog.show()
    }

    override fun setStatus(title: String, dayInfo: String, weekInfo: String) {
        toolbar.title = title
        textViewDayInfo.text = dayInfo
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
}
