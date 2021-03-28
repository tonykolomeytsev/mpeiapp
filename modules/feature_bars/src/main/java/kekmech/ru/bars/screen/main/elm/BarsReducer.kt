package kekmech.ru.bars.screen.main.elm

import kekmech.ru.bars.screen.main.elm.BarsEvent.News
import kekmech.ru.bars.screen.main.elm.BarsEvent.Wish
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
            effect = BarsEffect.LoadPage(event.remoteBarsConfig.loginLink)
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
        is Wish.Action.Update -> Result(state,
            effect = state.config?.loginLink?.let(BarsEffect::LoadPage))
        is Wish.Action.PageFinished -> {
            val isLoggedIn = state.config?.let { isLoggedInUrl(event.url, it) }
            Result(
                state = state.copy(
                    isLoggedIn = isLoggedIn,
                    config = if (isLoggedIn == true) state.config.copy(marksListLink = event.url) else state.config
                ),
                effect = state.config?.js?.extractDataDecoded?.let(BarsEffect::InvokeJs)
            )
        }

        is Wish.Click.ShowBrowser -> Result(state.copy(isBrowserShownForce = true))
        is Wish.Click.HideBrowser -> Result(state.copy(isBrowserShownForce = false))
        is Wish.Click.Notes -> Result(state, effect = BarsEffect.OpenAllNotes)
        is Wish.Click.Settings -> Result(state, effect = BarsEffect.OpenSettings)
        is Wish.Click.Logout -> Result(state, effect = BarsEffect.OpenSettings)
        is Wish.Click.SwipeToRefresh -> Result(state.copy(isLoading = true),
            effect = state.config?.marksListLink?.let(BarsEffect::LoadPage)
        )

        is Wish.Extract.StudentName -> Result(state, command = BarsAction.PushStudentName(event.name))
        is Wish.Extract.StudentGroup -> Result(state, command = BarsAction.PushStudentGroup(event.group))
        is Wish.Extract.MetaData -> Result(state) // TODO in future versions
        is Wish.Extract.Rating -> Result(state) // TODO in future versions
        is Wish.Extract.Semesters -> Result(state) // TODO in future versions
        is Wish.Extract.Marks -> Result(state, command = BarsAction.PushMarks(event.marksJson))
    }

    private fun isLoggedInUrl(url: String, config: RemoteBarsConfig): Boolean {
        return when {
            url.startsWith(config.marksListLink, ignoreCase = true) -> true
            else -> false
        }
    }
}