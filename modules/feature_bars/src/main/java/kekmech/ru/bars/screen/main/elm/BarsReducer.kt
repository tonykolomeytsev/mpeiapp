package kekmech.ru.bars.screen.main.elm

import android.webkit.CookieManager
import kekmech.ru.bars.screen.main.elm.BarsEvent.News
import kekmech.ru.bars.screen.main.elm.BarsEvent.Wish
import kekmech.ru.bars.screen.main.util.hasAuthCookie
import kekmech.ru.domain_bars.dto.JsKit
import kekmech.ru.domain_bars.dto.RemoteBarsConfig
import vivid.money.elmslie.core.store.Result
import vivid.money.elmslie.core.store.StateReducer

internal class BarsReducer : StateReducer<BarsEvent, BarsState, BarsEffect, BarsAction> {

    override fun reduce(
        event: BarsEvent,
        state: BarsState
    ): Result<BarsState, BarsEffect, BarsAction> = when (event) {
        is News -> reduceNews(event, state)
        is Wish -> reduceWish(event, state)
    }

    private fun reduceNews(
        event: News,
        state: BarsState
    ): Result<BarsState, BarsEffect, BarsAction> = when (event) {
        is News.GetRemoteBarsConfigSuccess -> Result(
            state = state.copy(config = event.remoteBarsConfig),
            effect = BarsEffect.LoadPage(event.remoteBarsConfig.loginUrl)
        )
        is News.GetRemoteBarsConfigFailure -> Result(state.copy(isAfterErrorLoadingConfig = true))
        is News.ObserveBarsSuccess -> Result(state.copy(userBars = event.userBars, isLoading = false))
        is News.ObserveBarsFailure -> Result(state.copy(isAfterErrorLoadingUserBars = true))
    }

    private fun reduceWish(
        event: Wish,
        state: BarsState
    ): Result<BarsState, BarsEffect, BarsAction> = when (event) {
        is Wish.Init -> Result(state,
            commands = listOf(
                BarsAction.GetRemoteBarsConfig,
                BarsAction.ObserveBars
            )
        )
        is Wish.Action.Update -> Result(state, effect = loadPageEffect(state) { loginUrl })
        is Wish.Action.PageFinished -> handlePageFinished(event, state)

        is Wish.Click.ShowBrowser -> Result(state.copy(isBrowserShownForce = true))
        is Wish.Click.HideBrowser -> Result(state.copy(isBrowserShownForce = false))
        is Wish.Click.Notes -> Result(state, effect = BarsEffect.OpenAllNotes)
        is Wish.Click.Settings -> Result(state, effect = BarsEffect.OpenSettings)
        is Wish.Click.Logout -> Result(state, effect = loadPageEffect(state) { logoutUrl })
        is Wish.Click.SwipeToRefresh -> Result(
            state = state.copy(isLoading = true),
            effect = loadPageEffect(state) { marksListUrl }
        )

        is Wish.Extract.StudentName -> Result(state, command = BarsAction.PushStudentName(event.name))
        is Wish.Extract.StudentGroup -> Result(state, command = BarsAction.PushStudentGroup(event.group))
        is Wish.Extract.MetaData -> Result(state) // TODO in future versions
        is Wish.Extract.Rating -> Result(state) // TODO in future versions
        is Wish.Extract.Semesters -> Result(state) // TODO in future versions
        is Wish.Extract.Marks -> Result(state, command = BarsAction.PushMarks(event.marksJson))
    }

    private fun handlePageFinished(
        event: Wish.Action.PageFinished,
        state: BarsState
    ): Result<BarsState, BarsEffect, BarsAction> {
        state.config ?: return Result(state)
        val oldFlowState = state.flowState
        val hasAuthCookie = CookieManager.getInstance().hasAuthCookie()
        return when {
            isMarksListUrl(event.url, state.config) && hasAuthCookie -> Result(
                state = state.copy(
                    flowState = FlowState.LOGGED_IN,
                    config = state.config.copy(marksListUrl = event.url),
                    isLoading = false,
                    isBrowserShownForce = oldFlowState == FlowState.NOT_LOGGED_IN
                ),
                effect = invokeJsEffect(state) { extractDataDecoded }
            )
            isMainPageUrl(event.url, state.config) && !hasAuthCookie -> Result(
                state.copy(
                    flowState = FlowState.NOT_LOGGED_IN,
                    isLoading = false
                )
            )
            else -> Result(
                state.copy(
                    flowState = FlowState.UNDEFINED,
                    isLoading = false
                )
            )
        }
    }

    private fun invokeJsEffect(state: BarsState, jsSelector: JsKit.() -> String) =
        state.config?.js?.jsSelector()?.let(BarsEffect::InvokeJs)

    private fun loadPageEffect(state: BarsState, urlSelector: RemoteBarsConfig.() -> String) =
        state.config?.urlSelector()?.let(BarsEffect::LoadPage)

    private fun isMarksListUrl(url: String, config: RemoteBarsConfig?) =
        if (config != null) url.startsWith(config.marksListUrl, ignoreCase = true) else false

    private fun isMainPageUrl(url: String, config: RemoteBarsConfig?) =
        if (config != null) url.normalizeUrl() == config.loginUrl.normalizeUrl() else false

    private fun String.normalizeUrl(): String = this
        .toLowerCase()
        .let { if (it.last() != '/') "$it/" else it }
}