package kekmech.ru.common_android

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager

public object KeyboardUtils {

    public fun showSoftInput(view: View) {
        view.let {
            val imm = view.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    public fun hideSoftInput(view: View) {
        view.let {
            val imm = view.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}
