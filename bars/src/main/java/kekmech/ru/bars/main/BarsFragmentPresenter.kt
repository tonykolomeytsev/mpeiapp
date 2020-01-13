package kekmech.ru.bars.main

import android.content.Context
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.bars.main.adapter.*
import kekmech.ru.bars.main.model.BarsFragmentModel
import kekmech.ru.bars.main.view.BarsFragmentView
import kekmech.ru.core.Presenter
import kekmech.ru.core.Router
import kekmech.ru.core.Screens.*
import kekmech.ru.core.UpdateChecker
import kekmech.ru.core.dto.AcademicScore
import kekmech.ru.coreui.adapter.BaseClickableItem
import kekmech.ru.coreui.adapter.BaseSortedAdapter
import kotlinx.coroutines.*

class BarsFragmentPresenter constructor(
    private val model: BarsFragmentModel,
    private val router: Router,
    private val updateChecker: UpdateChecker,
    private val context: Context
) : Presenter<BarsFragmentView>() {

    val recycledViewPool = RecyclerView.RecycledViewPool()
    val adapter = BaseSortedAdapter.Builder()
        .registerViewTypeFactory(ProfileItem.Factory())
        .registerViewTypeFactory(RatingItem.Factory())
        .registerViewTypeFactory(DisciplinesItem.Factory())
        .registerViewTypeFactory(SupportItem.Factory())
        .registerViewTypeFactory(BarsLoginItem.Factory())
        .allowOnlyUniqueItems()
        .addItemsOrder(listOf(
            BarsLoginItem::class,
            ProfileItem::class,
            RatingItem::class,
            DisciplinesItem::class,
            SupportItem::class
        ))
        .build()
    private var view: BarsFragmentView? = null
    private var cachedDisciplinesItem = DisciplinesItem(emptyList())

    override fun onResume(view: BarsFragmentView) {
        super.onResume(view)
        this.view = view
        if (model.isLoggedIn) {
            view.setLoginState(false)
            if (adapter.itemCount == 0) {
                adapter.setHasStableIds(true)
                GlobalScope.launch(Dispatchers.IO) {
                    model.updateScore()
                    view.hideLoading()
                }
            }
            model.score.observe(view, Observer {
                if (it == null) return@Observer
                updateWithScore(it)
                view.hideLoading()
            })
        } else {
            navigateToLogin()
        }

        view.onRefreshListener = {
            GlobalScope.launch(Dispatchers.IO) { model.updateScore() }
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

    private fun navigateToLogin() {
        adapter.removeItemByClass(ProfileItem::class)
        adapter.removeItemByClass(RatingItem::class)
        adapter.removeItemByClass(DisciplinesItem::class)
        adapter.removeItemByClass(SupportItem::class)
        GlobalScope.launch(Dispatchers.Main) {
            delay(100)
            adapter.addItem(BarsLoginItem(::logInUser, ::onRightsClick))
        }
        view?.setLoginState(true)
    }

    private fun onRightsClick() {
        router.navigate(BARS_TO_RIGHTS)
    }

    override fun onPause(view: BarsFragmentView) {
        super.onPause(view)
        this.view = null
    }

    private fun logInUser(login: String, pass: String, showError: () -> Unit) {
        model.saveUserSecrets(login, pass)
        GlobalScope.launch(Dispatchers.Main) {
            val score = withContext(Dispatchers.IO) { model.getAcademicScore(true) }
            if (score == null) {
                model.clearUserSecrets()
                showError()
            } else {
                view?.setLoginState(false)
                updateWithScore(score)
            }
        }
    }

    private fun updateWithScore(score: AcademicScore) {
        GlobalScope.launch(Dispatchers.Main) {
            adapter.removeItemByClass(BarsLoginItem::class)
            delay(100)
            try {
                val ri = RatingItem(score.rating).apply { clickListener = { onRatingClick(score) } }
                adapter.addItem(ri)
            } catch (e: Exception) { e.printStackTrace() }

            adapter.addItem(ProfileItem(score, ::logout))
            val disciplines = score.disciplines.map { DisciplineItem(it).apply { clickListener = ::onItemClick } }
            adapter.addItem(DisciplinesItem(disciplines))
            adapter.addItem(SupportItem(context))
        }
    }

    private fun onRatingClick(score: AcademicScore) {
        model.ratingDetails = score.rating
        router.navigate(BARS_TO_RATING)
    }

    private fun onItemClick(item: BaseClickableItem<*>) {
        if (item is DisciplineItem) {
            model.setCurrentDiscipline(item.discipline)
            router.navigate(BARS_TO_BARS_DETAILS)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun logout(v: View) {
        model.clearUserSecrets()
        navigateToLogin()
    }
}