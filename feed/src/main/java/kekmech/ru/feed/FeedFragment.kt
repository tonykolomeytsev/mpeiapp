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
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.feed.presenter.FeedPresenter


class FeedFragment @Inject constructor() : DaggerFragment(), IFeedFragment {

    @Inject
    lateinit var presenter: FeedPresenter

    init { retainInstance = true }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setOnApplyWindowInsetsListener { view, insets ->
            val statusBarSize = insets.systemWindowInsetTop
            val p = nestedScroll?.layoutParams as ViewGroup.MarginLayoutParams?
            p?.topMargin = -statusBarSize
            nestedScroll?.layoutParams = p
            nestedScroll?.setPadding(0, statusBarSize, 0, 0)
            insets
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        presenter.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause(this)
    }

    override fun withinContext(listener: (context: Context) -> Unit) {
        listener(requireContext())
    }

    override fun setAdapter(adapter: BaseAdapter) {
        recyclerView?.adapter = adapter
    }

    override fun hideLoading() {
        progressBar?.visibility = INVISIBLE
    }

    override fun showLoading() {
        progressBar?.visibility = VISIBLE
    }
}
