package kekmech.ru.feature_bars.screen.main.elm

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kekmech.ru.domain_bars.data.BarsConfigRepository
import kekmech.ru.domain_bars.data.BarsExtractJsRepository
import kekmech.ru.domain_bars.data.BarsUserInfoRepository
import kekmech.ru.feature_bars.screen.main.elm.BarsEvent.Internal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.rx3.rxCompletable
import kotlinx.coroutines.rx3.rxObservable
import kotlinx.coroutines.rx3.rxSingle
import vivid.money.elmslie.core.store.Actor
import java.util.concurrent.TimeUnit

internal class BarsActor(
    private val barsUserInfoRepository: BarsUserInfoRepository,
    private val barsConfigRepository: BarsConfigRepository,
    private val barsExtractJsRepository: BarsExtractJsRepository,
) : Actor<BarsCommand, BarsEvent> {

    override fun execute(command: BarsCommand): Observable<BarsEvent> = when (command) {
        is BarsCommand.GetRemoteBarsConfig -> Single.zip(
            rxSingle(Dispatchers.Unconfined) {
                barsConfigRepository.getBarsConfig().getOrThrow()
            },
            rxSingle(Dispatchers.Unconfined) {
                barsExtractJsRepository.getExtractJs().getOrThrow()
            },
            ::Pair,
        )
            .delay(CONFIG_DELAY, TimeUnit.MILLISECONDS)
            .mapSuccessEvent { (config, js) -> Internal.GetRemoteBarsConfigSuccess(config, js) }
            .mapErrorEvent(Internal::GetRemoteBarsConfigFailure)
        is BarsCommand.ObserveBars -> rxObservable(Dispatchers.Unconfined) {
            barsUserInfoRepository.observeBarsUserInfo().collect { send(it) }
        }.mapSuccessEvent(Internal::ObserveBarsSuccess)

        is BarsCommand.SetLatestLoadedUrl -> Completable
            .fromAction {
                barsUserInfoRepository.latestLoadedUrl = command.latestLoadedUrl.orEmpty()
            }
            .toObservable()
        is BarsCommand.GetLatestLoadedUrl -> Single
            .fromCallable { barsUserInfoRepository.latestLoadedUrl }
            .mapSuccessEvent { url -> Internal.GetLatestLoadedUrlSuccess(url.takeIf { it.isNotBlank() }) }

        is BarsCommand.PushMarks -> rxCompletable(Dispatchers.Unconfined) {
            barsUserInfoRepository.pushMarksJson(command.marksJson)
        }.toObservable()
        is BarsCommand.PushStudentName -> rxCompletable(Dispatchers.Unconfined) {
            barsUserInfoRepository.pushStudentName(command.studentName)
        }.toObservable()
        is BarsCommand.PushStudentGroup -> rxCompletable(Dispatchers.Unconfined) {
            barsUserInfoRepository.pushStudentGroup(command.studentGroup)
        }.toObservable()
        is BarsCommand.PushStudentRating -> rxCompletable(Dispatchers.Unconfined) {
            barsUserInfoRepository.pushStudentRating(command.ratingJson)
        }.toObservable()
    }

    private companion object {

        const val CONFIG_DELAY = 16L
    }
}
