package kekmech.ru.bars.screen.main.elm

import kekmech.ru.bars.screen.main.elm.BarsEvent.News
import kekmech.ru.bars.screen.main.elm.BarsEvent.Wish
import vivid.money.elmslie.core.store.Result
import vivid.money.elmslie.core.store.StateReducer

class BarsReducer : StateReducer<BarsEvent, BarsState, BarsEffect, BarsAction> {

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
        is News.GetRemoteBarsConfigFailure -> Result(state) // TODO handle error
    }

    private fun reduceWish(
        event: Wish,
        state: BarsState
    ): Result<BarsState, BarsEffect, BarsAction> = when (event) {
        is Wish.Init -> Result(state, command = BarsAction.GetRemoteBarsConfig)
        is Wish.Action.Update -> Result(state,
            effect = state.config?.loginLink?.let(BarsEffect::LoadPage))
        is Wish.Action.PageFinished -> Result(state,
            effect = state.config?.js?.extractData?.let(BarsEffect::InvokeJs))
    }
}