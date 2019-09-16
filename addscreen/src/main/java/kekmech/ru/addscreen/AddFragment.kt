package kekmech.ru.addscreen


import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import dagger.android.support.DaggerFragment
import kekmech.ru.core.Presenter
import kekmech.ru.coreui.Resources
import kotlinx.android.synthetic.main.fragment_add.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class AddFragment @Inject constructor() : DaggerFragment(), IAddFragment {

    @Inject lateinit var presenter: Presenter<IAddFragment>

    override val web: WebView get() = webView
    override lateinit var onSearchClickListener: (String) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity?.window?.statusBarColor = Resources.getColor(context, R.color.colorWhite)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume(this)
        buttonSelectGroup.setOnClickListener { onSearchClickListener(editTextGroupName.text.toString()) }
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause(this)
    }


}
