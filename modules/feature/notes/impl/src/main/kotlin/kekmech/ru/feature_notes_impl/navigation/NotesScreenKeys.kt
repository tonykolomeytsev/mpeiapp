package kekmech.ru.feature_notes_impl.navigation

import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.lib_navigation.ScreenKey
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class AllNotesScreenKey(val selectedNote: Note?) : ScreenKey

@Parcelize
internal data class NoteEditScreenKey(
    val note: Note,
    val resultKey: String,
) : ScreenKey
