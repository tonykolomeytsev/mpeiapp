package kekmech.ru.notes.view

import androidx.lifecycle.LifecycleOwner

interface NoteFragmentView : LifecycleOwner {
    var onBackNavClick: () -> Unit
    var onTextEdit: (String) -> Unit

    fun showSaved()
    fun setStatus(coupleName: String, coupleDate: String)
    fun setContent(coupleContent: String)
}