package kekmech.ru.feed

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.feed.presenter.FeedPresenter
import kotlinx.android.synthetic.main.fragment_feed.*
import org.koin.android.ext.android.inject


class FeedFragment constructor() : Fragment(), IFeedFragment {

    val presenter: FeedPresenter by inject()

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
