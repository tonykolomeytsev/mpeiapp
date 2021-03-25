package kekmech.ru.bars.screen.main.elm

import io.reactivex.Observable
import kekmech.ru.domain_bars.BarsRepository
import vivid.money.elmslie.core.store.Actor

class BarsActor(
    private val barsRepository: BarsRepository
) : Actor<BarsAction, BarsEvent> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(action: BarsAction): Observable<BarsEvent> = when (action) {
        is BarsAction.GetRemoteBarsConfig -> barsRepository.getRemoteBarsConfig()
            .mapEvents(BarsEvent.News::GetRemoteBarsConfigSuccess, BarsEvent.News::GetRemoteBarsConfigFailure)
    }
}