package kekmech.ru.bars.details.view

import android.os.Bundle
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.bars.R
import kekmech.ru.bars.details.BarsDetailsFragmentPresenter
import kekmech.ru.core.platform.BaseFragment
import kotlinx.android.synthetic.main.fragment_bars_details.*
import org.koin.android.ext.android.inject

class BarsDetailsFragment : BaseFragment<BarsDetailsFragmentPresenter, BarsDetailsFragmentView>(
    layoutId = R.layout.fragment_bars_details
), BarsDetailsFragmentView {

    override val presenter: BarsDetailsFragmentPresenter by inject()

    override var onNavBackListener: () -> Unit = {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        container1?.visibility = INVISIBLE
        container2?.visibility = INVISIBLE
        container3?.visibility = INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        toolbar?.setNavigationOnClickListener { onNavBackListener() }
        recyclerEvents?.layoutManager = LinearLayoutManager(context)
        recyclerWeeks?.layoutManager = LinearLayoutManager(context)
        recyclerFinal?.layoutManager = LinearLayoutManager(context)
    }

    override fun setEventsAdapter(adapter: RecyclerView.Adapter<*>) {
        progressBar?.visibility = GONE
        container1?.visibility = VISIBLE
        recyclerEvents?.adapter = adapter
    }

    override fun setWeeksAdapter(adapter: RecyclerView.Adapter<*>) {
        container2?.visibility = VISIBLE
        recyclerWeeks?.adapter = adapter
    }

    override fun setFinalAdapter(adapter: RecyclerView.Adapter<*>) {
        container3?.visibility = VISIBLE
        recyclerFinal?.adapter = adapter
        nestedScroll?.post { nestedScroll?.scrollTo(0, -1000) }
    }

    override fun setTitle(string: String) {
        textViewDisciplineHeader?.text = string
    }
}