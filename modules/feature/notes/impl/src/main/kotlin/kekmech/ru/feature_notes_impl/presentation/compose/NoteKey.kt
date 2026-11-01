package kekmech.ru.feature_notes_impl.presentation.compose

import kekmech.ru.feature_notes_api.domain.model.Note

/**
 * Stable key of the [Note] for lazy lists. Saved notes always have id.
 */
internal val Note.lazyKey: String
    get() = "note_${id ?: hashCode()}"
