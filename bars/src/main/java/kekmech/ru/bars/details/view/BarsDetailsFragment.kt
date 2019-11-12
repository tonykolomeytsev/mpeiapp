package kekmech.ru.bars.details.view

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.bars.R
import kekmech.ru.bars.details.BarsDetailsFragmentPresenter
import kekmech.ru.core.platform.BaseFragment
import kotlinx.android.synthetic.main.fragment_bars_details.*
import javax.inject.Inject

class BarsDetailsFragment : BaseFragment<BarsDetailsFragmentPresenter, BarsDetailsFragmentView>(
    layoutId = R.layout.fragment_bars_details
), BarsDetailsFragmentView {
    @Inject
    override lateinit var presenter: BarsDetailsFragmentPresenter

    override var onNavBackListener: () -> Unit = {}

    override fun onResume() {
        super.onResume()
        toolbar?.setNavigationOnClickListener { onNavBackListener() }
        recyclerEvents?.layoutManager = LinearLayoutManager(context)
        recyclerWeeks?.layoutManager = LinearLayoutManager(context)
        recyclerFinal?.layoutManager = LinearLayoutManager(context)
    }

    override fun setEventsAdapter(adapter: RecyclerView.Adapter<*>) {
        recyclerEvents?.adapter = adapter
    }

    override fun setWeeksAdapter(adapter: RecyclerView.Adapter<*>) {
        recyclerWeeks?.adapter = adapter
    }

    override fun setFinalAdapter(adapter: RecyclerView.Adapter<*>) {
        recyclerFinal?.adapter = adapter
    }

    override fun setTitle(string: String) {
        textViewDisciplineHeader?.text = string
    }
}