package kekmech.ru.bars.main.view

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.bars.R
import kekmech.ru.bars.main.BarsFragmentPresenter
import kekmech.ru.core.platform.BaseFragment
import kotlinx.android.synthetic.main.fragment_bars.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class BarsFragment : BaseFragment<BarsFragmentPresenter, BarsFragmentView>(
    layoutId = R.layout.fragment_bars
), BarsFragmentView {

    @Inject
    override lateinit var presenter: BarsFragmentPresenter

    override fun onResume() {
        super.onResume()
        recyclerDisciplines?.layoutManager = LinearLayoutManager(context)
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        progressBar?.visibility = View.INVISIBLE
        recyclerDisciplines?.adapter = adapter
    }
}