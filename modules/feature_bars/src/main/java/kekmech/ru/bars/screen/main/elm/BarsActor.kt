package kekmech.ru.bars.screen.main.elm

import io.reactivex.Observable
import kekmech.ru.bars.screen.main.elm.BarsEvent.News
import kekmech.ru.domain_bars.BarsRepository
import vivid.money.elmslie.core.store.Actor

internal class BarsActor(
    private val barsRepository: BarsRepository
) : Actor<BarsAction, BarsEvent> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(action: BarsAction): Observable<BarsEvent> = when (action) {
        is BarsAction.GetRemoteBarsConfig -> barsRepository.getRemoteBarsConfig()
            .mapEvents(News::GetRemoteBarsConfigSuccess, News::GetRemoteBarsConfigFailure)
        is BarsAction.ObserveBars -> barsRepository.observeUserBars()
            .mapEvents(News::ObserveBarsSuccess, News::ObserveBarsFailure)
        is BarsAction.PushMarks -> barsRepository.pushMarksJson(action.marksJson)
            .toObservable()
        is BarsAction.PushStudentName -> barsRepository.pushStudentName(action.studentName)
            .toObservable()
        is BarsAction.PushStudentGroup -> barsRepository.pushStudentGroup(action.studentGroup)
            .toObservable()
    }
}