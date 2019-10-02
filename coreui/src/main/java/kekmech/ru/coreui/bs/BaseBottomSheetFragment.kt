package kekmech.ru.coreui.bs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment(
    private val layoutId: Int
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false)

    fun onClick(buttonId: Int, action: (View) -> Unit) {
        val button = view?.findViewById<View>(buttonId)
        if (button != null) {
            button.setOnClickListener(action)
        } else {
            Log.e("BaseBottomSheetFragment", "Button with id $buttonId not found!")
        }
    }
}