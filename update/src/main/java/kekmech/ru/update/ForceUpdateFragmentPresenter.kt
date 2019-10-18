package kekmech.ru.update

import kekmech.ru.core.Presenter
import kekmech.ru.update.model.ForceUpdateFragmentModel
import kekmech.ru.update.view.ForceUpdateFragmentView
import javax.inject.Inject

class ForceUpdateFragmentPresenter @Inject constructor(
    private val model: ForceUpdateFragmentModel
) : Presenter<ForceUpdateFragmentView>() {

}