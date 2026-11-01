package kekmech.ru.feature_notes_impl.presentation.screen.edit

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.kekmech.lib_fragment.ComposeFragment
import com.kekmech.lib_fragment.LocalResultController
import com.kekmech.lib_fragment.LocalRouter
import kekmech.ru.ext_android.EmptyResult
import kekmech.ru.ext_android.hideKeyboard
import kekmech.ru.ext_android.notNullSerializableArg
import kekmech.ru.ext_android.notNullStringArg
import kekmech.ru.ext_android.withArguments
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_notes_impl.di.NotesDependencies
import kekmech.ru.feature_notes_impl.presentation.screen.edit.elm.NoteEditEffect
import kekmech.ru.feature_notes_impl.presentation.screen.edit.elm.NoteEditEvent.Ui
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import kekmech.ru.lib_elm_compose.EffectHandler
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.android.ext.android.inject
import kekmech.ru.res_strings.R.string as Strings

internal class NoteEditFragment : ComposeFragment() {

    private val dependencies: NotesDependencies by inject()
    private val analytics by screenAnalytics("NoteEdit")
    private val resultKey by fastLazy { notNullStringArg(ARG_RESULT_KEY) }

    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }

    @Composable
    override fun Screen() {
        MpeixTheme {
            val router = LocalRouter.current
            val resultController = LocalResultController.current
            val context = LocalContext.current
            ElmContent(
                key = "NoteEditScreen",
                storeFactory = {
                    dependencies.noteEditStoreFactory.create(note = notNullSerializableArg(ARG_NOTE))
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
                            resultController.setResult(resultKey, EmptyResult)
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

    companion object {

        private const val ARG_NOTE = "Arg.Note"
        private const val ARG_RESULT_KEY = "Arg.ResultKey"

        fun newInstance(
            note: Note,
            resultKey: String,
        ) = NoteEditFragment()
            .withArguments(
                ARG_NOTE to note,
                ARG_RESULT_KEY to resultKey,
            )
    }
}
