package kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.utils

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

private val LabelModifierPattern = "[а-яА-Я]+-[0-9]+([а-яА-Я])+(-[0-9]+)?".toRegex()
private val SingleDigitGroupNumberPattern = "[а-яА-Я]+-([0-9])[а-яА-Я]*-([0-9]+)?".toRegex()

/**
 * Normalizes group number while user is typing:
 * - capitalizes faculty label: `с-12-16` -> `С-12-16`
 * - lowercases label modifiers: `С-12М-16` -> `С-12м-16`
 * - adds leading zero to course number: `С-6-` -> `С-06-`
 */
internal class GroupNumberFormatter(facultyLabels: List<String>) {

    private val facultyLabels = facultyLabels.sortedByDescending { it.length }

    fun format(value: TextFieldValue): TextFieldValue {
        val modifiedString = ModifiedString(value.text)
            .let(::capitalizeFacultyLabel)
            .let(::lowerCaseForLabelModifiers)
            .let(::doubleDigitGroupNumber)
        if (modifiedString.string == value.text) return value
        val length = modifiedString.string.length
        return value.copy(
            text = modifiedString.string,
            selection = TextRange(
                start = (value.selection.start + modifiedString.selectionOffset).coerceIn(0, length),
                end = (value.selection.end + modifiedString.selectionOffset).coerceIn(0, length),
            ),
        )
    }

    private fun capitalizeFacultyLabel(s: ModifiedString): ModifiedString {
        return facultyLabels
            .map { it to s.string.take(it.length) }
            .firstOrNull { (label, selection) -> label.equals(selection, ignoreCase = true) }
            ?.takeIf { (label, selection) -> label != selection }
            ?.let { (label, selection) ->
                s.copy(
                    string = s.string.replaceFirst(selection, label)
                )
            } ?: s
    }

    private fun lowerCaseForLabelModifiers(s: ModifiedString): ModifiedString {
        return LabelModifierPattern
            .find(s.string)
            ?.groups?.get(1)
            ?.let { it.value to it.range }
            ?.takeIf { (value, _) -> value != value.lowercase() }
            ?.let { (value, range) ->
                s.copy(
                    string = s.string.replaceRange(range, value.lowercase())
                )
            } ?: s
    }

    private fun doubleDigitGroupNumber(s: ModifiedString): ModifiedString {
        return SingleDigitGroupNumberPattern
            .find(s.string)
            ?.groups?.get(1)
            ?.let { it.value to it.range }
            ?.let { (value, range) ->
                s.copy(
                    string = s.string.replaceRange(range, "0$value"),
                    selectionOffset = 1
                )
            } ?: s
    }

    private data class ModifiedString(
        val string: String,
        val selectionOffset: Int = 0
    )
}
