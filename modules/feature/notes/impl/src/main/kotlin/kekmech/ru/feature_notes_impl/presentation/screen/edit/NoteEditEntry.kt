package kekmech.ru.feature_notes_impl.presentation.screen.edit

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.kekmech.lib_fragment.HideKeyboardOnPause
import com.kekmech.lib_fragment.LocalResultController
import com.kekmech.lib_fragment.LocalRouter
import kekmech.ru.ext_android.EmptyResult
import kekmech.ru.feature_notes_impl.di.NotesDependencies
import kekmech.ru.feature_notes_impl.navigation.NoteEditScreenKey
import kekmech.ru.feature_notes_impl.presentation.screen.edit.elm.NoteEditEffect
import kekmech.ru.feature_notes_impl.presentation.screen.edit.elm.NoteEditEvent.Ui
import kekmech.ru.lib_analytics_android.ext.rememberScreenAnalytics
import kekmech.ru.lib_elm_compose.EffectHandler
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.compose.koinInject
import kekmech.ru.res_strings.R.string as Strings

@Composable
internal fun NoteEditEntry(key: NoteEditScreenKey) {
    val dependencies = koinInject<NotesDependencies>()
    val analytics = rememberScreenAnalytics("NoteEdit")
    HideKeyboardOnPause()
    MpeixTheme {
        val router = LocalRouter.current
        val resultController = LocalResultController.current
        val context = LocalContext.current
        ElmContent(
            key = "NoteEditScreen",
            storeFactory = {
                dependencies.noteEditStoreFactory.create(note = key.note)
            },
        ) { onAccept, state, effects ->
            NoteEditScreen(
                note = state.note,
                onBackClick = { router.executeCommand(PopBackStack()) },
                onContentChange = { content -> onAccept(Ui.Action.NoteContentChanged(content)) },
                onSaveClick = {
                    analytics.sendClick("SaveNote")
                    onAccept(Ui.Click.SaveNote)
                },
            )
            EffectHandler(effects) { effect ->
                when (effect) {
                    is NoteEditEffect.CloseWithSuccess -> {
                        resultController.setResult(key.resultKey, EmptyResult)
                        router.executeCommand(PopBackStack())
                    }

                    is NoteEditEffect.ShowError -> Toast
                        .makeText(context, Strings.something_went_wrong_error, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
