package kekmech.ru.bars.main.view

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
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

    init {
        retainInstance = true
    }

    override val presenter: BarsFragmentPresenter by inject()

    override var onRefreshListener: () -> Unit = {}

    override fun onResume() {
        super.onResume()
        if (recyclerView?.layoutManager == null)
            recyclerView?.layoutManager = LinearLayoutManager(context)
        if (recyclerView?.adapter == null)
            recyclerView?.adapter = presenter.adapter

        swipeRefresh?.setProgressViewEndTarget(false, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 144f, resources.displayMetrics).toInt())
        swipeRefresh?.setOnRefreshListener { onRefreshListener() }
        swipeRefresh?.setColorSchemeColors(
            Resources.getColor(context, R.color.colorPrimary),
            Resources.getColor(context, R.color.colorSecondary)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedView?.parent != null) (savedView?.parent as ViewGroup?)?.removeView(savedView)
        if (savedView == null) savedView = super.onCreateView(inflater, container, savedInstanceState)
        return savedView!!
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

    override fun setLoginState(boolean: Boolean) {
        if (boolean) {
            swipeRefresh?.isRefreshing = false
            swipeRefresh?.isEnabled = false
        } else {
            swipeRefresh?.isEnabled = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("main_rv", recyclerView?.layoutManager?.onSaveInstanceState())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            if (recyclerView?.layoutManager == null) recyclerView?.layoutManager = LinearLayoutManager(context)
            recyclerView?.layoutManager?.onRestoreInstanceState(
                savedInstanceState.getParcelable("main_rv")
            )
        }
    }

    override fun onAttach(context: Context) {
        retainInstance = true
        super.onAttach(context)
    }

    companion object {
        private var savedView: View? = null
    }
}