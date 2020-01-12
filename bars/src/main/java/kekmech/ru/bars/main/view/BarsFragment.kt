package kekmech.ru.bars.main.view

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.bars.R
import kekmech.ru.bars.main.BarsFragmentPresenter
import kekmech.ru.core.platform.BaseFragment
import kekmech.ru.coreui.Resources
import kotlinx.android.synthetic.main.fragment_bars.*
import org.koin.android.ext.android.inject


class BarsFragment : BaseFragment<BarsFragmentPresenter, BarsFragmentView>(
    layoutId = R.layout.fragment_bars
), BarsFragmentView {

    override val presenter: BarsFragmentPresenter by inject()

    override var onRefreshListener: () -> Unit = {}

    override fun onResume() {
        super.onResume()
        recyclerView?.setRecycledViewPool(presenter.recycledViewPool)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = presenter.adapter

        swipeRefresh?.setProgressViewEndTarget(false, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 144f, resources.displayMetrics).toInt())
        swipeRefresh?.setOnRefreshListener { onRefreshListener() }
        swipeRefresh?.setColorSchemeColors(
            Resources.getColor(context, R.color.colorPrimary),
            Resources.getColor(context, R.color.colorSecondary)
        )
    }

    override fun hideLoading() {
        swipeRefresh?.post { swipeRefresh?.isRefreshing = false }
    }

    override fun showLoading() {
        swipeRefresh?.post { swipeRefresh?.isRefreshing = true }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setOnApplyWindowInsetsListener { view, insets ->
            val statusBarSize = insets.systemWindowInsetTop
            val p = swipeRefresh?.layoutParams as ViewGroup.MarginLayoutParams?
            p?.topMargin = -statusBarSize
            swipeRefresh?.layoutParams = p
            swipeRefresh?.setPadding(0, statusBarSize, 0, 0)
            insets
        }
        super.onViewCreated(view, savedInstanceState)
    }
}