package kekmech.ru.addscreen


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.util.TypedValue
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import dagger.android.support.DaggerFragment
import kekmech.ru.core.Presenter
import kekmech.ru.coreui.Resources
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.web_view_stub.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext
import android.view.KeyEvent.KEYCODE_ENTER
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import kekmech.ru.coreui.adapter.BaseAdapter
import java.lang.Exception


/**
 * A simple [Fragment] subclass.
 */
class AddFragment @Inject constructor() : BottomSheetDialogFragment(), HasAndroidInjector, IAddFragment {

    @Inject
    @JvmField
    public var androidInjector: DispatchingAndroidInjector<Any>? = null

    @Inject lateinit var presenter: Presenter<IAddFragment>

    override val web: WebView get() = view?.findViewById(R.id.webView)!!
    override lateinit var onSearchClickListener: (String) -> Unit

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View?)?.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        textView2.setOnClickListener { editTextGroupName.setText("С-12-17") }
        editTextGroupName.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                    onSearchClickListener(editTextGroupName.text.toString())
                    return true
                }
                return false
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.setOnShowListener {
            val bsInternal = dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bsInternal).peekHeight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                500f,
                resources.displayMetrics
            ).toInt()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume(this)
        buttonSelectGroup?.setOnClickListener { onSearchClickListener(editTextGroupName?.text.toString()) }
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause(this)
    }

    override fun showLoadButton() {
        buttonSelectGroup?.isClickable = true
        buttonSelectGroup
            ?.animate()
            ?.alpha(1f)
            ?.setDuration(200)
            ?.start()
    }

    override fun hideLoadButton() {
        buttonSelectGroup?.isClickable = false
        buttonSelectGroup
            ?.animate()
            ?.alpha(0f)
            ?.setDuration(200)
            ?.start()
    }

    override fun disableEditText() {
        editTextGroupName?.clearFocus()
        editTextGroupName?.isEnabled = false
    }

    override fun enableEditText() {
        editTextGroupName?.isEnabled = true
    }

    override fun showLoading() {
        frameLayoutLoading?.animate()
            ?.alpha(1f)
            ?.setDuration(200)
            ?.withStartAction { frameLayoutLoading?.visibility = View.VISIBLE }
            ?.start()
        nestedScrollGroups?.visibility = View.GONE
    }

    override fun hideLoading() {
        frameLayoutLoading?.visibility = View.GONE
        nestedScrollGroups?.visibility = View.VISIBLE
    }

    override fun setAdapter(adapter: BaseAdapter) {
        progressBarGroups?.visibility = View.INVISIBLE
        recyclerViewGroups?.layoutManager = LinearLayoutManager(context)
        recyclerViewGroups?.adapter = adapter
    }

}
