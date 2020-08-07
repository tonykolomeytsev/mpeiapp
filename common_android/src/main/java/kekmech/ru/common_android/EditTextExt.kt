package kekmech.ru.common_android

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

fun EditText.showKeyboard() {
    requestFocus()
    post { KeyboardUtils.showSoftInput(this) }
}

fun EditText.afterTextChanged(callback: (text: String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        override fun afterTextChanged(s: Editable?) {
            callback(s.toString())
        }
    })
}

fun TextInputLayout.clearErrorAfterChange() {
    editText?.afterTextChanged { error = null }
}