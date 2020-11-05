package kekmech.ru.feature_app_settings.screens.edit_favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kekmech.ru.common_android.*
import kekmech.ru.feature_app_settings.R
import kotlinx.android.synthetic.main.fragment_edit_favorite.*

private const val ARG_GROUP_NUMBER = "Arg.GroupNumber"
private const val ARG_DESCRIPTION = "Arg.Description"

internal class EditFavoriteFragment : Fragment(R.layout.fragment_edit_favorite) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener { close() }
        header.text = getArgument<String>(ARG_GROUP_NUMBER)
        val description = getArgument<String>(ARG_DESCRIPTION)
        groupText.setText(description)
        groupText.setSelection(description.length)
        groupText.showKeyboard()
        buttonContinue.setOnClickListener {
            closeWithResult {
                putExtra(EXTRA_GROUP_NAME, getArgument<String>(ARG_GROUP_NUMBER))
                putExtra(EXTRA_DESCRIPTION, groupText.text.toString())
            }
        }
    }

    companion object {

        const val EXTRA_GROUP_NAME = "group_number"
        const val EXTRA_DESCRIPTION = "description"

        fun newInstance(
            groupNumber: String,
            description: String = ""
        ) = EditFavoriteFragment().withArguments(
            ARG_GROUP_NUMBER to groupNumber,
            ARG_DESCRIPTION to description
        )
    }

}