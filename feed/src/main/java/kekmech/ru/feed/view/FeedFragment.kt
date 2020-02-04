package kekmech.ru.feed.view

import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.feed.FeedViewModel
import kekmech.ru.feed.R
import kotlinx.android.synthetic.main.fragment_feed.*
import org.koin.android.ext.android.inject

class FeedFragment : Fragment(R.layout.fragment_feed) {

    private val viewModel: FeedViewModel by inject()

    init { retainInstance = true }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setOnApplyWindowInsetsListener { _, insets ->
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
        viewModel.updateSchedules()
        viewModel.checkForEmptyChedule()
        viewModel.checkForUpdates()
        viewModel.updateActualSchedules()

        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.adapter = viewModel.adapter
        viewModel.feedItems.observe(this, Observer {
            viewModel.adapter.updateDataSet(it)
        })

        viewModel.isLoading.observe(this, Observer {
            progressBar?.visibility = if (it == true) VISIBLE else INVISIBLE
        })

        buttonSettings?.setOnClickListener { viewModel.navigateToSettings() }
    }
}
