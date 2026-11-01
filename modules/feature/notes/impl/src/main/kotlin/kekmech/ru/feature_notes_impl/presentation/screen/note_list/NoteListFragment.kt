package kekmech.ru.feature_notes_impl.presentation.screen.note_list

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.kekmech.lib_fragment.ComposeBottomSheetDialogFragment
import com.kekmech.lib_fragment.LocalRouter
import kekmech.ru.ext_android.close
import kekmech.ru.ext_android.notNullSerializableArg
import kekmech.ru.ext_android.notNullStringArg
import kekmech.ru.ext_android.withArguments
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_notes_impl.di.NotesDependencies
import kekmech.ru.feature_notes_impl.presentation.screen.edit.NoteEditFragment
import kekmech.ru.feature_notes_impl.presentation.screen.note_list.elm.NoteListEffect
import kekmech.ru.feature_notes_impl.presentation.screen.note_list.elm.NoteListEvent.Ui
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import kekmech.ru.lib_elm_compose.EffectHandler
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.lib_navigation.AddScreenForward
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.android.ext.android.inject
import java.time.LocalDate
import kekmech.ru.res_strings.R.string as Strings

internal class NoteListFragment : ComposeBottomSheetDialogFragment() {

    private val dependencies by inject<NotesDependencies>()
    private val analytics by screenAnalytics("NoteList")
    private val resultKey by fastLazy { notNullStringArg(ARG_RESULT_KEY) }

    @Composable
    override fun Screen() {
        MpeixTheme {
            val router = LocalRouter.current
            val context = LocalContext.current
            ElmContent(
                key = "NoteListScreen",
                storeFactory = {
                    dependencies.noteListStoreFactory.create(
                        selectedClasses = notNullSerializableArg(ARG_SELECTED_CLASSES),
                        selectedDate = notNullSerializableArg(ARG_SELECTED_DATE),
                    )
                },
            ) { onAccept, state, effects ->
                NoteListScreen(
                    notes = state.notes,
                    onNoteClick = { note ->
                        analytics.sendClick("EditNote")
                        onAccept(Ui.Click.EditNote(note))
                    },
                    onNoteDelete = { note ->
                        analytics.sendClick("DeleteNote")
                        onAccept(Ui.Action.DeleteNote(note))
                    },
                    onCreateNoteClick = {
                        analytics.sendClick("NewNote")
                        onAccept(Ui.Click.CreateNewNote)
                    },
                )
                EffectHandler(effects) { effect ->
                    when (effect) {
                        is NoteListEffect.ShowNoteLoadError -> Toast
                            .makeText(context, Strings.something_went_wrong_error, Toast.LENGTH_SHORT)
                            .show()

                        is NoteListEffect.OpenNoteEdit -> {
                            close()
                            router.executeCommand(AddScreenForward {
                                NoteEditFragment.newInstance(effect.note, resultKey)
                            })
                        }
                    }
                }
            }
        }
    }

    companion object {

        private const val ARG_SELECTED_CLASSES = "Arg.SelectedClasses"
        private const val ARG_SELECTED_DATE = "Arg.SelectedDate"
        private const val ARG_RESULT_KEY = "Arg.ResultKey"

        fun newInstance(
            selectedClasses: Classes,
            selectedDate: LocalDate,
            resultKey: String,
        ): ComposeBottomSheetDialogFragment = NoteListFragment()
            .withArguments(
                ARG_SELECTED_CLASSES to selectedClasses,
                ARG_SELECTED_DATE to selectedDate,
                ARG_RESULT_KEY to resultKey,
            )
    }
}
