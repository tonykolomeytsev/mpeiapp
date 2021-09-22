package kekmech.ru.feature_app_settings.screens.edit_favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kekmech.ru.common_analytics.ext.screenAnalytics
import kekmech.ru.common_android.*
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.feature_app_settings.R
import kekmech.ru.feature_app_settings.databinding.FragmentEditFavoriteBinding

internal class EditFavoriteFragment : Fragment(R.layout.fragment_edit_favorite) {

    private val viewBinding by viewBinding(FragmentEditFavoriteBinding::bind)
    private val analytics by screenAnalytics("EditFavorite")
    private val resultKey by fastLazy { getArgument<String>(ARG_RESULT_KEY) }
    private val groupNumber by fastLazy { getArgument<String>(ARG_GROUP_NUMBER) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            appBarLayout.addSystemTopPadding()
            root.addSystemBottomPadding()

            toolbar.setNavigationOnClickListener { close() }
            header.text = groupNumber
            val description = getArgument<String>(ARG_DESCRIPTION)
            groupText.setText(description)
            groupText.setSelection(description.length)
            groupText.showKeyboard()
            buttonContinue.setOnClickListener {
                analytics.sendClick("SaveFavoriteDescription")
                close()
                setResult(resultKey, result = groupNumber to groupText.text.toString())
            }
        }
    }

    companion object {

        private const val ARG_GROUP_NUMBER = "Arg.GroupNumber"
        private const val ARG_DESCRIPTION = "Arg.Description"
        private const val ARG_RESULT_KEY = "Arg.ResultKey"

        fun newInstance(
            groupNumber: String,
            description: String,
            resultKey: String,
        ) = EditFavoriteFragment().withArguments(
            ARG_GROUP_NUMBER to groupNumber,
            ARG_DESCRIPTION to description,
            ARG_RESULT_KEY to resultKey,
        )
    }

}