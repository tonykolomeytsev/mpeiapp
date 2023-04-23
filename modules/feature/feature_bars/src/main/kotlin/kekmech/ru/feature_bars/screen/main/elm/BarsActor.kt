package kekmech.ru.feature_bars.screen.main.elm

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kekmech.ru.domain_bars.BarsRepository
import kekmech.ru.feature_bars.screen.main.elm.BarsEvent.Internal
import vivid.money.elmslie.core.store.Actor
import java.util.concurrent.TimeUnit

internal class BarsActor(
    private val barsRepository: BarsRepository,
) : Actor<BarsCommand, BarsEvent> {

    override fun execute(command: BarsCommand): Observable<BarsEvent> = when (command) {
        is BarsCommand.GetRemoteBarsConfig -> Single.zip(
            barsRepository.getRemoteBarsConfig(),
            barsRepository.getExtractJs(),
            ::Pair
        )
            .delay(CONFIG_DELAY, TimeUnit.MILLISECONDS)
            .mapSuccessEvent { (config, js) -> Internal.GetRemoteBarsConfigSuccess(config, js) }
            .mapErrorEvent(Internal::GetRemoteBarsConfigFailure)
        is BarsCommand.ObserveBars -> barsRepository.observeUserBars()
            .mapSuccessEvent(Internal::ObserveBarsSuccess)
        is BarsCommand.SetLatestLoadedUrl -> Completable
            .fromAction { barsRepository.latestLoadedUrl = command.latestLoadedUrl.orEmpty() }
            .toObservable()
        is BarsCommand.GetLatestLoadedUrl -> Single
            .fromCallable { barsRepository.latestLoadedUrl }
            .mapSuccessEvent { Internal.GetLatestLoadedUrlSuccess(it.takeIf { it.isNotBlank() }) }

        is BarsCommand.PushMarks -> barsRepository.pushMarksJson(command.marksJson)
            .onErrorComplete()
            .toObservable()
        is BarsCommand.PushStudentName -> barsRepository.pushStudentName(command.studentName)
            .onErrorComplete()
            .toObservable()
        is BarsCommand.PushStudentGroup -> barsRepository.pushStudentGroup(command.studentGroup)
            .onErrorComplete()
            .toObservable()
        is BarsCommand.PushStudentRating -> barsRepository.pushStudentRating(command.ratingJson)
            .onErrorComplete()
            .toObservable()
    }

    private companion object {

        const val CONFIG_DELAY = 16L
    }
}
