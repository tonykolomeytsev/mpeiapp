package kekmech.ru.bars.screen.main.elm

import vivid.money.elmslie.core.store.Result
import vivid.money.elmslie.core.store.StateReducer

class BarsReducer : StateReducer<BarsEvent, BarsState, BarsEffect, BarsAction> {

    override fun reduce(
        event: BarsEvent,
        state: BarsState
    ): Result<BarsState, BarsEffect, BarsAction> {
        TODO("Not yet implemented")
    }
}