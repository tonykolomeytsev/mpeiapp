package kekmech.ru.bars.screen.main.elm

import io.reactivex.Observable
import vivid.money.elmslie.core.store.Actor

class BarsActor : Actor<BarsAction, BarsEvent> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(action: BarsAction): Observable<BarsEvent> {
        TODO()
    }
}