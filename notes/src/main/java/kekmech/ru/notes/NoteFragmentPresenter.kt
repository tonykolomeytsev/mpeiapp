package kekmech.ru.notes

import kekmech.ru.core.Presenter
import kekmech.ru.core.Router
import kekmech.ru.notes.model.NoteFragmentModel
import kekmech.ru.notes.view.NoteFragmentView
import javax.inject.Inject

class NoteFragmentPresenter @Inject constructor(
    private val model: NoteFragmentModel,
    private val router: Router
): Presenter<NoteFragmentView>() {

    override fun onResume(view: NoteFragmentView) {
        super.onResume(view)
        view.onBackNavClick = router::popBackStack
    }
}