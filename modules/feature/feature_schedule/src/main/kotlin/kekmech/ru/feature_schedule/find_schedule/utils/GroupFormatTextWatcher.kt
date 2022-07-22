package kekmech.ru.feature_schedule.find_schedule.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kekmech.ru.common_android.getStringArray
import kekmech.ru.coreui.R as coreui_R

internal class GroupFormatTextWatcher(
    private val editText: EditText
) : TextWatcher {

    private val facultyLabels = editText.context.getStringArray(coreui_R.array.faculties_labels)
        .sortedByDescending { it.length }

    override fun afterTextChanged(s: Editable?) = Unit

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)  = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        s ?: return
        val modifiedString = ModifiedString(s.toString())
            .let { capitalizeFacultyLabel(it) }
            .let { lowerCaseForLabelModifiers(it) }
            .let { doubleDigitGroupNumber(it) }
        if (s.toString() != modifiedString.string) {
            var selection = editText.selectionStart
            editText.setText(modifiedString.string)
            selection += modifiedString.selectionOffset
            editText.setSelection(selection)
        }
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
        return "[а-яА-Я]+-[0-9]+([а-яА-Я])+(-[0-9]+)?".toRegex()
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
        return "[а-яА-Я]+-([0-9])[а-яА-Я]*-([0-9]+)?".toRegex()
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
