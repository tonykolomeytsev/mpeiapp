package kekmech.ru.update.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kekmech.ru.update.ForceUpdateFragmentPresenter
import kekmech.ru.update.R
import kotlinx.android.synthetic.main.fragment_force_update.*
import javax.inject.Inject

class ForceUpdateFragment : BottomSheetDialogFragment(), HasAndroidInjector, ForceUpdateFragmentView {

    @Inject
    @JvmField
    public var androidInjector: DispatchingAndroidInjector<Any>? = null

    @Inject
    lateinit var presenter: ForceUpdateFragmentPresenter

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

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector!!

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