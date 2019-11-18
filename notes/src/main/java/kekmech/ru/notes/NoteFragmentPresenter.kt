package kekmech.ru.notes

import kekmech.ru.core.Presenter
import kekmech.ru.notes.model.NoteFragmentModel
import kekmech.ru.notes.view.NoteFragmentView
import javax.inject.Inject

class NoteFragmentPresenter @Inject constructor(
    private val model: NoteFragmentModel
): Presenter<NoteFragmentView>() {

}