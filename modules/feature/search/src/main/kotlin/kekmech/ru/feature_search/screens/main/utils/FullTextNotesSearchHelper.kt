package kekmech.ru.feature_search.screens.main.utils

import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.feature_search.screens.main.simplify

internal class FullTextNotesSearchHelper(
    private val notes: List<Note>,
    private val query: String
) {
    fun execute() = notes
        .filter { note -> predicates.any { it(note, query) } }

    companion object {
        private val predicates = listOf<(Note, String) -> Boolean>(
            { n, q -> q in n.classesName.simplify() },
            { n, q -> q in n.content.simplify() }
        )
    }
}
