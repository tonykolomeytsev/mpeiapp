package kekmech.ru.core.platform

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kekmech.ru.core.Presenter

@Suppress("UNCHECKED_CAST")
abstract class BaseBottomSheetDialogFragment<P : Presenter<L>, L : LifecycleOwner>(
    private val layoutId: Int,
    private val peekHeight: Float  = -1f
) : BottomSheetDialogFragment() {

    abstract var presenter: P

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View?)?.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (peekHeight != -1f) {
            dialog?.setOnShowListener {
                val bsInternal =
                    dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                BottomSheetBehavior.from(bsInternal).peekHeight = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    peekHeight,
                    resources.displayMetrics
                ).toInt()
            }
        }
        presenter.onCreate(this as L)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BaseKeyboardObserver(view as ViewGroup, this::onKeyboardVisibilityChanged)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume(this as L)
    }

    override fun onPause() {
        presenter.onPause(this as L)
        super.onPause()
    }

    open fun onKeyboardVisibilityChanged(visible: Boolean) = Unit
}