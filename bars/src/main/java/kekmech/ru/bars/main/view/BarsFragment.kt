package kekmech.ru.bars.main.view

import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.bars.R
import kekmech.ru.bars.main.BarsFragmentPresenter
import kekmech.ru.bars.main.view.BarsFragmentView.State
import kekmech.ru.bars.main.view.BarsFragmentView.State.LOGIN
import kekmech.ru.bars.main.view.BarsFragmentView.State.SCORE
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

    override var onLogInListener: (String, String) -> Unit = {_,_->}

    override var state: State = LOGIN
        set(value) {
            field = value
            onStateChanged()
        }

    override fun onResume() {
        super.onResume()
        recyclerDisciplines?.layoutManager = LinearLayoutManager(context)
        textViewBarsPass?.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    onLogInListener(textViewBarsLogin.text.toString(), textViewBarsPass.text.toString())
                    return true
                }
                return false
            }
        })
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        progressBar?.visibility = View.INVISIBLE
        recyclerDisciplines?.adapter = adapter
    }

    private fun onStateChanged() {
        if (state == SCORE) {
            loginLayout?.visibility = View.INVISIBLE
            recyclerDisciplines?.visibility = View.VISIBLE
        } else if (state == LOGIN) {
            loginLayout?.visibility = View.VISIBLE
            recyclerDisciplines?.visibility = View.INVISIBLE
        }
    }

}