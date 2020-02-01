package kekmech.ru.notes.model

import android.util.Base64
import com.google.gson.GsonBuilder
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.NoteNative
import kekmech.ru.core.dto.NoteTimestamp
import kekmech.ru.core.usecases.GetCreateNoteTransactionUseCase
import kekmech.ru.core.usecases.GetNoteByIdUseCase
import kekmech.ru.core.usecases.GetNoteByTimestampUseCase
import kekmech.ru.core.usecases.SaveNoteUseCase

class NoteFragmentModelImpl constructor(
    private val getCreateNoteTransactionUseCase: GetCreateNoteTransactionUseCase,
    private val getNoteByTimestampUseCase: GetNoteByTimestampUseCase,
    private val saveNoteUseCase: SaveNoteUseCase
) : NoteFragmentModel {
    private val gson = GsonBuilder().create()
    override val transactedCouple: CoupleNative?
        get() = getCreateNoteTransactionUseCase()?.coupleNative
    override val transactedRealWeek: Int?
        get() = getCreateNoteTransactionUseCase()?.realWeek

    override fun getNoteContent(): String {
        val native = getNoteByTimestampUseCase(timestamp())
        if (native == null) {
            return ""
        } else {
            val decoded = fromBase64(native.data)
            val note = gson.fromJson(decoded, NoteNative.Note::class.java)
            return note.text
        }
    }

    override fun saveNote(note: NoteNative.Note) {
        val encoded = toBase64(gson.toJson(note))
        val native = NoteNative(-1, timestamp(), encoded, "")
        saveNoteUseCase(native, note.text.isBlank())
    }

    private fun fromBase64(string: String): String {
        return Base64.decode(string.toByteArray(Charsets.UTF_8), Base64.DEFAULT).toString(Charsets.UTF_8)
    }

    private fun toBase64(string: String): String {
        return Base64.encode(string.toByteArray(Charsets.UTF_8), Base64.DEFAULT).toString(Charsets.UTF_8)
    }

    private fun timestamp() =
        "w${transactedRealWeek}d${transactedCouple?.day}n${transactedCouple?.num}h${transactedCouple?.place?.trim().hashCode()}"
}