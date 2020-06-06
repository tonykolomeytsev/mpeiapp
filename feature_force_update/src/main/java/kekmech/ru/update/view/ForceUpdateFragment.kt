package kekmech.ru.update.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kekmech.ru.update.ForceUpdateFragmentPresenter
import kekmech.ru.update.R
import kotlinx.android.synthetic.main.fragment_force_update.*
import org.koin.android.ext.android.inject

class ForceUpdateFragment : BottomSheetDialogFragment(), ForceUpdateFragmentView {

    val presenter: ForceUpdateFragmentPresenter by inject()

    override var onUpdateNow: () -> Unit = {}

    override var onUpdateLater: () -> Unit = {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View?)?.setBackgroundColor(Color.TRANSPARENT)
        if (presenter.model.description.isNotBlank()) {
            textDescription?.text = presenter.model.description
            textDescription?.visibility = View.VISIBLE
        } else {
            textDescription?.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_force_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonUpdateNow?.setOnClickListener { onUpdateNow() }
        buttonUpdateLater?.setOnClickListener { onUpdateLater() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause(this)
    }
}