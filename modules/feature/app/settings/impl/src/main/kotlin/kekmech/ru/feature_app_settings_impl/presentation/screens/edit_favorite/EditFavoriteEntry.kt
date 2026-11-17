package kekmech.ru.feature_app_settings_impl.presentation.screens.edit_favorite

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.result.LocalResultEventBus
import kekmech.ru.feature_app_settings_impl.navigation.EditFavoriteScreenKey
import kekmech.ru.lib_navigation.HideKeyboardOnPause
import kekmech.ru.lib_navigation.LocalRouter
import kekmech.ru.lib_navigation.PopBackStack

@Composable
internal fun EditFavoriteEntry(key: EditFavoriteScreenKey) {
    HideKeyboardOnPause()
    val router = LocalRouter.current
    val resultEventBus = LocalResultEventBus.current
    EditFavoriteScreen(
        name = key.favoriteSchedule.name,
        initialDescription = key.favoriteSchedule.description,
        onBackClick = { router.executeCommand(PopBackStack()) },
        onSaveClick = { description ->
            resultEventBus.sendResult(
                key.resultKey,
                key.favoriteSchedule.copy(description = description),
            )
            router.executeCommand(PopBackStack())
        },
    )
}
