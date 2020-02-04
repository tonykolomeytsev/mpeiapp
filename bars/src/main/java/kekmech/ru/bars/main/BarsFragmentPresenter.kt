package kekmech.ru.bars.main

import android.content.Context
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import kekmech.ru.bars.main.adapter.*
import kekmech.ru.bars.main.model.BarsFragmentModel
import kekmech.ru.bars.main.view.BarsFragmentView
import kekmech.ru.core.Presenter
import kekmech.ru.core.Router
import kekmech.ru.core.Screens.*
import kekmech.ru.core.UpdateChecker
import kekmech.ru.core.dto.AcademicScore
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseClickableItem
import kekmech.ru.coreui.adapter.BaseItem
import kotlinx.coroutines.*
import kotlin.random.Random

class BarsFragmentPresenter constructor(
    private val model: BarsFragmentModel,
    private val router: Router,
    private val updateChecker: UpdateChecker,
    private val context: Context
) : Presenter<BarsFragmentView>() {

    val adapter = BaseAdapter.Builder()
        .registerViewTypeFactory(ProfileItem.Factory())
        .registerViewTypeFactory(RatingItem.Factory())
        .registerViewTypeFactory(DisciplineItem.Factory())
        .registerViewTypeFactory(SupportItem.Factory())
        .registerViewTypeFactory(BarsLoginItem.Factory())
        .build()
    private var view: BarsFragmentView? = null
    private fun getDiffCallbackFor(newListOfItems: List<BaseItem<*>>) = object : DiffUtil.Callback() {
        override fun getNewListSize() = newListOfItems.size
        override fun getOldListSize() = adapter.items.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            adapter.items[oldItemPosition].javaClass == newListOfItems[newItemPosition].javaClass

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            adapter.items[oldItemPosition] == newListOfItems[newItemPosition]
    }

    override fun onResume(view: BarsFragmentView) {
        super.onResume(view)
        this.view = view
        if (model.isLoggedIn) {
            view.setLoginState(false)
            if (adapter.itemCount == 0) {
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
        GlobalScope.launch(Dispatchers.Main) {
            val newListOfItems = mutableListOf<BaseItem<*>>(BarsLoginItem(::logInUser, ::onRightsClick))
            val diffResult = withContext(Dispatchers.Default) {
                DiffUtil.calculateDiff(getDiffCallbackFor(newListOfItems), true)
            }
            adapter.items.clear()
            adapter.items.addAll(newListOfItems)
            diffResult.dispatchUpdatesTo(adapter)
            view?.setLoginState(true)
        }
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

            val newListOfItems = mutableListOf<BaseItem<*>>()
            newListOfItems += ProfileItem(score, ::logout)
            try {
                val ri = RatingItem(score.rating).apply { clickListener = { onRatingClick(score) } }
                newListOfItems += ri
            } catch (e: Exception) { e.printStackTrace() }

            newListOfItems += score.disciplines.map { DisciplineItem(it).apply { clickListener = ::onItemClick } }
            newListOfItems += SupportItem(context)

            val diffResult = withContext(Dispatchers.Main) {
                DiffUtil.calculateDiff(getDiffCallbackFor(newListOfItems), true)
            }
            adapter.items.clear()
            adapter.items.addAll(newListOfItems)
            diffResult.dispatchUpdatesTo(adapter)
            diffResult.dispatchUpdatesTo(object : ListUpdateCallback {
                override fun onChanged(position: Int, count: Int, payload: Any?) = println("onChanged: $position $count")

                override fun onMoved(fromPosition: Int, toPosition: Int) = println("onMoved: $fromPosition $toPosition")

                override fun onInserted(position: Int, count: Int) = println("onInserted: $position $count")

                override fun onRemoved(position: Int, count: Int) = println("onRemoved: $position $count")
            })
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