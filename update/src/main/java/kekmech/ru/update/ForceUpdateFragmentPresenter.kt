package kekmech.ru.update

import kekmech.ru.core.Presenter
import kekmech.ru.core.Router
import kekmech.ru.update.model.ForceUpdateFragmentModel
import kekmech.ru.update.view.ForceUpdateFragmentView
import javax.inject.Inject

class ForceUpdateFragmentPresenter @Inject constructor(
    private val model: ForceUpdateFragmentModel,
    private val router: Router
) : Presenter<ForceUpdateFragmentView>() {

    override fun onResume(view: ForceUpdateFragmentView) {
        super.onResume(view)
        view.onUpdateNow = this::onUpdateNow
        view.onUpdateLater = router::popBackStack
    }

    private fun onUpdateNow() {
        println("onUpdateNow")
        router.popBackStack()
    }
}