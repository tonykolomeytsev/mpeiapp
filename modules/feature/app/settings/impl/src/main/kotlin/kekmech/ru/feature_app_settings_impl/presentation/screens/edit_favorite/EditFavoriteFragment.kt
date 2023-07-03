package kekmech.ru.feature_app_settings_impl.presentation.screens.edit_favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kekmech.ru.ext_android.addSystemBottomPadding
import kekmech.ru.ext_android.addSystemTopPadding
import kekmech.ru.ext_android.close
import kekmech.ru.ext_android.getArgument
import kekmech.ru.ext_android.hideKeyboard
import kekmech.ru.ext_android.setResult
import kekmech.ru.ext_android.showKeyboard
import kekmech.ru.ext_android.viewbinding.viewBinding
import kekmech.ru.ext_android.withArguments
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_app_settings_impl.R
import kekmech.ru.feature_app_settings_impl.databinding.FragmentEditFavoriteBinding
import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule
import kekmech.ru.library_analytics_android.ext.screenAnalytics

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
        ) = EditFavoriteFragment()
            .withArguments(
                ArgFavoriteSchedule to favoriteSchedule,
                ArgResultKey to resultKey,
            )
    }

}
