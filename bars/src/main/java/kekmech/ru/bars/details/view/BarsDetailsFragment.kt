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
import javax.inject.Inject

class BarsDetailsFragment : BaseFragment<BarsDetailsFragmentPresenter, BarsDetailsFragmentView>(
    layoutId = R.layout.fragment_bars_details
), BarsDetailsFragmentView {
    @Inject
    override lateinit var presenter: BarsDetailsFragmentPresenter

    override var onNavBackListener: () -> Unit = {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewHeader1?.visibility = INVISIBLE
        textViewHeader2?.visibility = INVISIBLE
        textViewHeader3?.visibility = INVISIBLE
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
        textViewHeader1?.visibility = VISIBLE
        recyclerEvents?.adapter = adapter
    }

    override fun setWeeksAdapter(adapter: RecyclerView.Adapter<*>) {
        textViewHeader2?.visibility = VISIBLE
        recyclerWeeks?.adapter = adapter
    }

    override fun setFinalAdapter(adapter: RecyclerView.Adapter<*>) {
        textViewHeader3?.visibility = VISIBLE
        recyclerFinal?.adapter = adapter
    }

    override fun setTitle(string: String) {
        textViewDisciplineHeader?.text = string
    }
}