package kekmech.ru.coreui.bs

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kekmech.ru.coreui.R

abstract class BaseBottomSheetFragment(
    private val layoutId: Int
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false)

    override fun getTheme() = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?) = BottomSheetDialog(requireContext(), theme)

    fun onClick(buttonId: Int, action: (View) -> Unit): BaseBottomSheetFragment {
        val button = view?.findViewById<View>(buttonId)
        if (button != null) {
            button.setOnClickListener(action)
        } else {
            Log.e("BaseBottomSheetFragment", "Button with id $buttonId not found!")
        }
        return this
    }
}