package kekmech.ru.feature_app_settings_impl.presentation.screens.edit_favorite

import androidx.compose.runtime.Composable
import com.kekmech.lib_fragment.HideKeyboardOnPause
import com.kekmech.lib_fragment.LocalResultController
import com.kekmech.lib_fragment.LocalRouter
import kekmech.ru.feature_app_settings_impl.navigation.EditFavoriteScreenKey
import kekmech.ru.lib_navigation.PopBackStack

@Composable
internal fun EditFavoriteEntry(key: EditFavoriteScreenKey) {
    HideKeyboardOnPause()
    val router = LocalRouter.current
    val resultController = LocalResultController.current
    EditFavoriteScreen(
        name = key.favoriteSchedule.name,
        initialDescription = key.favoriteSchedule.description,
        onBackClick = { router.executeCommand(PopBackStack()) },
        onSaveClick = { description ->
            resultController.setResult(
                key.resultKey,
                key.favoriteSchedule.copy(description = description),
            )
            router.executeCommand(PopBackStack())
        },
    )
}
