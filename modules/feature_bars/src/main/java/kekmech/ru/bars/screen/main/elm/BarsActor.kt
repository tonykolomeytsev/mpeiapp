package kekmech.ru.bars.screen.main.elm

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kekmech.ru.bars.screen.main.elm.BarsEvent.News
import kekmech.ru.domain_bars.BarsRepository
import vivid.money.elmslie.core.store.Actor

internal class BarsActor(
    private val barsRepository: BarsRepository
) : Actor<BarsAction, BarsEvent> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(action: BarsAction): Observable<BarsEvent> = when (action) {
        is BarsAction.GetRemoteBarsConfig -> Single.zip(
            barsRepository.getRemoteBarsConfig(),
            barsRepository.getExtractJs(),
            ::Pair
        )
            .mapSuccessEvent { (config, js) -> News.GetRemoteBarsConfigSuccess(config, js) }
            .mapErrorEvent(News::GetRemoteBarsConfigFailure)
        is BarsAction.ObserveBars -> barsRepository.observeUserBars()
            .mapSuccessEvent(News::ObserveBarsSuccess)
        is BarsAction.PushMarks -> barsRepository.pushMarksJson(action.marksJson)
            .toObservable()
        is BarsAction.PushStudentName -> barsRepository.pushStudentName(action.studentName)
            .toObservable()
        is BarsAction.PushStudentGroup -> barsRepository.pushStudentGroup(action.studentGroup)
            .toObservable()
    }
}