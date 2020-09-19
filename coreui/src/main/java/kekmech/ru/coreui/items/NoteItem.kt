package kekmech.ru.coreui.items

import kekmech.ru.coreui.R
import java.time.LocalDate

data class NoteItem(
    val content: String,
    val date: LocalDate,
    val disciplineName: String
)

class NoteAdapterItem : ClickableAdapterItem<NoteItem>(
    isType = { it is NoteItem },
    layoutRes = R.layout.item_note
)