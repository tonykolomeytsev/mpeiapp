package kekmech.ru.bars.main

import android.content.Context
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import kekmech.ru.bars.main.adapter.*
import kekmech.ru.bars.main.model.BarsFragmentModel
import kekmech.ru.core.*
import kekmech.ru.core.Screens.*
import kekmech.ru.core.dto.AcademicScore
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseClickableItem
import kekmech.ru.coreui.adapter.BaseItem
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class BarsViewModel constructor(
    private val model: BarsFragmentModel,
    private val router: Router,
    private val updateChecker: UpdateChecker,
    private val context: Context
) : ViewModel() {

    val adapter = BaseAdapter.Builder()
        .registerViewTypeFactory(ProfileItem.Factory())
        .registerViewTypeFactory(RatingItem.Factory())
        .registerViewTypeFactory(DisciplineItem.Factory())
        .registerViewTypeFactory(SupportItem.Factory())
        .registerViewTypeFactory(BarsLoginItem.Factory())
        .registerViewTypeFactory(SyncInProgressItem.Factory())
        .build()
    val barsState = MutableLiveData<Boolean>().apply { value = false }
    val contentItems: LiveData<List<BaseItem<*>>> = Transformations.map(zipNullable(model.isLoggedIn, model.score)) { (isLoggedIn, score) ->
        if (isLoggedIn == true && score != null) {
            barsState.value = true
            scoreItems(score)
        } else if (isLoggedIn == true || model.hasUserCredentials) {
            barsState.value = false
            listOf(SyncInProgressItem())
        } else {
            barsState.value = false
            listOf(BarsLoginItem(::logInUser, ::onRightsClick))
        }
    }
    val loadUrl = LiveEvent<Pair<String, (String) -> Unit>>()
    val executeScript = LiveEvent<Triple<String, (String) -> Unit, (String) -> Unit>>()

    private fun getDiffCallbackFor(newListOfItems: List<BaseItem<*>>) = object : DiffUtil.Callback() {
        override fun getNewListSize() = newListOfItems.size
        override fun getOldListSize() = adapter.items.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            adapter.items[oldItemPosition].javaClass == newListOfItems[newItemPosition].javaClass

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            adapter.items[oldItemPosition] == newListOfItems[newItemPosition]
    }

    fun checkForUpdates() {
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

    private fun onRightsClick() = router.navigate(BARS_TO_RIGHTS)

    private fun logInUser(login: String, pass: String, showError: () -> Unit) {

/*        model.saveUserSecrets(login, pass)
        GlobalScope.launch(Dispatchers.Main) {
            val score = withContext(Dispatchers.IO) { model.getAcademicScore(true) }
            if (score == null) {
                model.clearUserSecrets()
                showError()
            } else {
                view?.setLoginState(false)
                scoreItems(score)
            }
        }*/
    }

    private fun scoreItems(score: AcademicScore) = listOfNotNull(
        ProfileItem(score, ::logout),
        try { RatingItem(score.rating).apply { clickListener = { onRatingClick(score) } } } catch (e: Exception) { null },
        *score.disciplines.map { DisciplineItem(it).apply { clickListener = ::onItemClick } }.toTypedArray(),
        SupportItem(context)
    )

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
    private fun logout(v: View) = model.logout()

    fun refresh() {
        if (model.isLoggedIn.value == true) GlobalScope.launch(IO) {
            model.updateScore()
        } else {
            val barsMainPage = "https://bars.mpei.ru/bars_web/"
            loadUrl(barsMainPage to { url ->
                Log.d("BarsViewModel", "loaded url: $url")
                if (url == barsMainPage) executeScript(Triple(
                    model.getLoginScript(),
                    { e -> Log.d("BarsViewModel", "script: " + e) },
                    { redirectUrl ->
                        Log.d("BarsViewModel", "redirect url: $redirectUrl")
                        if (redirectUrl.contains(".*Part1.*".toRegex()) || redirectUrl.contains(".*Auth.*".toRegex())) GlobalScope.launch(IO) {
                            model.updateScore()
                        }
                    }
                )) else {
                    if (url.contains(".*Part1.*".toRegex()) || url.contains(".*Auth.*".toRegex())) GlobalScope.launch(IO) {
                        model.updateScore()
                    }
                }
            })
        }
    }

    fun updateAdapter(listOfItems: List<BaseItem<*>>) {
        DiffUtil.calculateDiff(getDiffCallbackFor(listOfItems)).let {
            adapter.items.clear()
            adapter.items.addAll(listOfItems)
            try {
                it.dispatchUpdatesTo(adapter)
            } catch (e: Exception) {
                adapter.notifyDataSetChanged()
            }
        }
    }
}