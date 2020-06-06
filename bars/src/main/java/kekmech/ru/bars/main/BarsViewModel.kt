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
import kekmech.ru.core.exceptions.NotLoggedInBarsException
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
    val alert = MutableLiveData<Boolean>().apply { value = true }
    val contentItems: LiveData<List<BaseItem<*>>> = Transformations.map(zipNullable(model.isLoggedIn, model.score, alert)) { (isLoggedIn, score, _) ->
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
    var onErrorListener: () -> Unit = {}
    val userAgent = listOf(
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36 OPR/66.0.3515.72",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 Edge/18.18362",
        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.93 Safari/537.36"
    ).random()

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
        onErrorListener = showError
        model.saveUserSecrets(login, pass)
        try {
            logInBars()
        } catch (e: NotLoggedInBarsException) {
            showError()
            model.logout()
        }
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
    private fun logout(v: View) {
        model.logout()
        loadUrl("https://bars.mpei.ru/bars_web/Auth/Exit" to {_->})
    }

    fun refresh() {
        if (model.isLoggedIn.value == true) GlobalScope.launch(IO) {
            model.updateScore()
        } else {
            try { logInBars() } catch (e: NotLoggedInBarsException) { e.printStackTrace() }
        }
    }

    fun setUserAgent(ua: String) {
        model.setUserAgent(ua)
    }

    private fun logInBars() {
        val barsMainPage = "https://bars.mpei.ru/bars_web/"
        loadUrl(barsMainPage to { url ->
            if (url == barsMainPage) executeScript(Triple(
                model.getLoginScript(),
                { _->},
                { url ->
                    if (url.contains(".*Part1.*".toRegex()) || url.contains(".*Auth.*".toRegex())) GlobalScope.launch(IO) {
                        model.updateScore()
                    } else {
                        adapter.items.firstOrNull { it is BarsLoginItem }
                            ?.let { (it as BarsLoginItem).error() }
                        onErrorListener()
                        executeScript(Triple("!!(document.querySelector('div[class*=alert]'))", { result ->
                            if (result == "true") {
                                model.logout()
                                alert.value = !(alert.value == true)
                            }
                        }, {_->}))
                    }
                }
            )) else {
                if (url.contains(".*Part1.*".toRegex()) || url.contains(".*Auth.*".toRegex())) GlobalScope.launch(IO) {
                    model.updateScore()
                }
            }
        })
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