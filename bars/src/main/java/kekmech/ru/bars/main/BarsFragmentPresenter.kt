package kekmech.ru.bars.main

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.bars.main.adapter.DisciplineItem
import kekmech.ru.bars.main.model.BarsFragmentModel
import kekmech.ru.bars.main.view.BarsFragmentView
import kekmech.ru.core.Presenter
import kekmech.ru.core.Router
import kekmech.ru.core.Screens
import kekmech.ru.core.Screens.*
import kekmech.ru.core.UpdateChecker
import kekmech.ru.core.dto.AcademicDiscipline
import kekmech.ru.core.dto.AcademicScore
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseClickableItem
import kotlinx.coroutines.*
import javax.inject.Inject

class BarsFragmentPresenter @Inject constructor(
    private val model: BarsFragmentModel,
    private val router: Router,
    private val updateChecker: UpdateChecker
) : Presenter<BarsFragmentView>() {

    val recycledViewPool = RecyclerView.RecycledViewPool()
    private val adapter = BaseAdapter.Builder()
        .registerViewTypeFactory(DisciplineItem.Factory())
        .build()
    private var view: BarsFragmentView? = null

    override fun onResume(view: BarsFragmentView) {
        super.onResume(view)
        this.view = view
        // change view state as fast as possible
        if (model.isLoggedIn) {
            view.state = BarsFragmentView.State.SCORE
            GlobalScope.launch(Dispatchers.IO) {
                delay(50)
                model.getAcademicScoreAsync { score ->
                    GlobalScope.launch(Dispatchers.Main) {
                        updateWithScore(view, score)
                    }
                }
            }
        } else {
            view.state = BarsFragmentView.State.LOGIN
        }
        view.onLogInListener = ::logInUser
        view.onRightsClickListener = { router.navigate(BARS_TO_RIGHTS) }
        view.onLogoutListener = {
            Log.d("Bars", "LOGOUT")
            model.clearUserSecrets()
            view.state = BarsFragmentView.State.LOGIN
        }
        view.onRefreshListener = {
            GlobalScope.launch(Dispatchers.IO) { model.getAcademicScoreAsync(true) {
                GlobalScope.launch(Dispatchers.Main) { updateWithScore(view, it) } } }
        }
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000)
            if (model.isNotShowedUpdateDialog) {
                updateChecker.check { url, desc ->
                    model.saveForceUpdateArgs(url, desc)
                    router.navigate(BARS_TO_FORCE)
                    model.isNotShowedUpdateDialog = false
                }
            }
        }
    }

    override fun onPause(view: BarsFragmentView) {
        super.onPause(view)
        this.view = null
    }

    private fun logInUser(login: String, pass: String) {
        model.saveUserSecrets(login, pass)
        view?.setLoginFormEnabled(false)
        GlobalScope.launch(Dispatchers.Main) {
            var error = false
            val score = withContext(Dispatchers.IO) { model.getAcademicScore() }
            if (score == null) {
                Log.d("Bars", "Wrong User Credentials")
                model.clearUserSecrets()
                error = true
            } else {
                if (view != null) {
                    view?.state = BarsFragmentView.State.SCORE
                    updateWithScore(view!!, score)
                }
            }
            view?.setLoginFormEnabled(true)
            if (error)
                view?.showError()
        }
    }

    private fun updateWithScore(view: BarsFragmentView, score: AcademicScore?) {
        if (score != null) {
            adapter.baseItems.clear()
            adapter.baseItems.addAll(score.disciplines.map { DisciplineItem(it).apply { clickListener = ::onItemClick } })
            view.setAdapter(adapter)
            view.setStatus(score)
        }
        view.hideLoading()
    }

    private fun onItemClick(item: BaseClickableItem<*>) {
        if (item is DisciplineItem) {
            model.setCurrentDiscipline(item.discipline)
            router.navigate(BARS_TO_BARS_DETAILS)
        }
    }
}