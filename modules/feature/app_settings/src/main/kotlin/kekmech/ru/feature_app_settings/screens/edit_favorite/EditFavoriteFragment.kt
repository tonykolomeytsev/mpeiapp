package kekmech.ru.feature_app_settings.screens.edit_favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kekmech.ru.common_analytics.ext.screenAnalytics
import kekmech.ru.common_android.addSystemBottomPadding
import kekmech.ru.common_android.addSystemTopPadding
import kekmech.ru.common_android.close
import kekmech.ru.common_android.getArgument
import kekmech.ru.common_android.hideKeyboard
import kekmech.ru.common_android.setResult
import kekmech.ru.common_android.showKeyboard
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_android.withArguments
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.domain_favorite_schedule.dto.FavoriteSchedule
import kekmech.ru.feature_app_settings.R
import kekmech.ru.feature_app_settings.databinding.FragmentEditFavoriteBinding

internal class EditFavoriteFragment : Fragment(R.layout.fragment_edit_favorite) {

    private val viewBinding by viewBinding(FragmentEditFavoriteBinding::bind)
    private val analytics by screenAnalytics("EditFavorite")
    private val resultKey by fastLazy { getArgument<String>(ArgResultKey) }
    private val favoriteSchedule by fastLazy { getArgument<FavoriteSchedule>(ArgFavoriteSchedule) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            appBarLayout.addSystemTopPadding()
            root.addSystemBottomPadding()

            toolbar.setNavigationOnClickListener {
                close()
                hideKeyboard()
            }
            header.text = favoriteSchedule.name
            groupText.setText(favoriteSchedule.description)
            groupText.setSelection(favoriteSchedule.description.length)
            groupText.showKeyboard()
            buttonContinue.setOnClickListener {
                analytics.sendClick("SaveFavoriteDescription")
                close()
                hideKeyboard()
                setResult(
                    key = resultKey,
                    result = favoriteSchedule.copy(description = groupText.text.toString()),
                )
            }
        }
    }

    companion object {

        private const val ArgFavoriteSchedule = "ArgFavoriteSchedule"
        private const val ArgResultKey = "ArgResultKey"

        fun newInstance(
            favoriteSchedule: FavoriteSchedule,
            resultKey: String,
        ) = EditFavoriteFragment().withArguments(
            ArgFavoriteSchedule to favoriteSchedule,
            ArgResultKey to resultKey,
        )
    }

}