package kekmech.ru.feature_app_settings_impl.presentation.screens.edit_favorite

import androidx.compose.runtime.Composable
import com.kekmech.lib_fragment.ComposeFragment
import com.kekmech.lib_fragment.LocalResultController
import com.kekmech.lib_fragment.LocalRouter
import kekmech.ru.ext_android.hideKeyboard
import kekmech.ru.ext_android.notNullSerializableArg
import kekmech.ru.ext_android.notNullStringArg
import kekmech.ru.ext_android.withArguments
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.ui_theme.theme.MpeixTheme

internal class EditFavoriteFragment : ComposeFragment() {

    private val resultKey by fastLazy { notNullStringArg(ArgResultKey) }
    private val favoriteSchedule by fastLazy {
        notNullSerializableArg<FavoriteSchedule>(ArgFavoriteSchedule)
    }

    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }

    @Composable
    override fun Screen() {
        MpeixTheme {
            val router = LocalRouter.current
            val resultController = LocalResultController.current
            EditFavoriteScreen(
                name = favoriteSchedule.name,
                initialDescription = favoriteSchedule.description,
                onBackClick = { router.executeCommand(PopBackStack()) },
                onSaveClick = { description ->
                    resultController.setResult(
                        resultKey,
                        favoriteSchedule.copy(description = description),
                    )
                    router.executeCommand(PopBackStack())
                },
            )
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
