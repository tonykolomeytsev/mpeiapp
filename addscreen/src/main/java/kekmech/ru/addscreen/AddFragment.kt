package kekmech.ru.addscreen


import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kekmech.ru.addscreen.presenter.AddFragmentPresenter
import kekmech.ru.core.usecases.IsDarkThemeEnabledUseCase
import kekmech.ru.coreui.Resources
import kotlinx.android.synthetic.main.fragment_add.*
import org.koin.android.ext.android.inject


/**
 * A simple [Fragment] subclass.
 */
class AddFragment : BottomSheetDialogFragment(), IAddFragment {

    private val presenter: AddFragmentPresenter by inject()
    private val isDarkThemeEnabledUseCase: IsDarkThemeEnabledUseCase by inject()

    override lateinit var onSearchClickListener: (String) -> Unit

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View?)?.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        textView2.setOnClickListener { editTextGroupName.setText(R.string.default_group_name) }
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
            val bsInternal = dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) ?: return@setOnShowListener
            BottomSheetBehavior.from(bsInternal).peekHeight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                500f,
                resources.displayMetrics
            ).toInt()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) ?: return@setOnShowListener
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.skipCollapsed = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1 && !isDarkThemeEnabledUseCase()) {
            setWhiteNavigationBar(bottomSheetDialog)
        }
        return bottomSheetDialog
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun setWhiteNavigationBar(@NonNull dialog: Dialog) {
        val window = dialog.window
        if (window != null) {
            val metrics = DisplayMetrics()
            window.windowManager.defaultDisplay.getMetrics(metrics)

            val dimDrawable = GradientDrawable()
            // ...customize your dim effect here

            val navigationBarDrawable = GradientDrawable()
            navigationBarDrawable.shape = GradientDrawable.RECTANGLE
            navigationBarDrawable.setColor(Resources.getColor(requireContext(), R.color.colorLightGray))

            val layers = arrayOf<Drawable>(dimDrawable, navigationBarDrawable)

            val windowBackground = LayerDrawable(layers)
            windowBackground.setLayerInsetTop(1, metrics.heightPixels)

            window.setBackgroundDrawable(windowBackground)
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

    override fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        progressBarGroups?.visibility = View.INVISIBLE
        recyclerViewGroups?.layoutManager = LinearLayoutManager(context)
        recyclerViewGroups?.adapter = adapter
    }

    override fun showError() {
        Toast.makeText(context, "Не удалозь загрузить группу", Toast.LENGTH_SHORT).show()
    }

}
