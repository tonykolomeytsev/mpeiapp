package kekmech.ru.bars.main

import kekmech.ru.bars.main.model.BarsFragmentModel
import kekmech.ru.bars.main.view.BarsFragmentView
import kekmech.ru.core.Presenter
import javax.inject.Inject

class BarsFragmentPresenter @Inject constructor(
    private val model: BarsFragmentModel
) : Presenter<BarsFragmentView>() {

}