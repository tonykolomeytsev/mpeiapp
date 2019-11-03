package kekmech.ru.bars.main

import kekmech.ru.bars.main.adapter.DisciplineItem
import kekmech.ru.bars.main.model.BarsFragmentModel
import kekmech.ru.bars.main.view.BarsFragmentView
import kekmech.ru.core.Presenter
import kekmech.ru.core.dto.AcademicDiscipline
import kekmech.ru.coreui.adapter.BaseAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class BarsFragmentPresenter @Inject constructor(
    private val model: BarsFragmentModel
) : Presenter<BarsFragmentView>() {

    private val adapter = BaseAdapter.Builder()
        .registerViewTypeFactory(DisciplineItem.Factory())
        .build()

    override fun onResume(view: BarsFragmentView) {
        super.onResume(view)
        // change view state as fast as possible
        if (model.isLoggedIn) {
            view.state = BarsFragmentView.State.SCORE
            GlobalScope.launch(Dispatchers.Main) {
                delay(100)
                adapter.baseItems.addAll((0..10).map { DisciplineItem(AcademicDiscipline()) })
                view.setAdapter(adapter)
            }
        } else {
            view.state = BarsFragmentView.State.LOGIN
        }
        view.onLogInListener = model::logInUser
    }
}