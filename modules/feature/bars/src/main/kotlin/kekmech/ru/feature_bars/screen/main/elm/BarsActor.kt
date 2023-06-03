package kekmech.ru.feature_bars.screen.main.elm

import kekmech.ru.common_elm.actorFlow
import kekmech.ru.domain_bars.data.BarsConfigRepository
import kekmech.ru.domain_bars.data.BarsExtractJsRepository
import kekmech.ru.domain_bars.data.BarsUserInfoRepository
import kekmech.ru.feature_bars.screen.main.elm.BarsEvent.Internal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.coroutines.Actor

internal class BarsActor(
    private val barsUserInfoRepository: BarsUserInfoRepository,
    private val barsConfigRepository: BarsConfigRepository,
    private val barsExtractJsRepository: BarsExtractJsRepository,
) : Actor<BarsCommand, BarsEvent> {

    override fun execute(command: BarsCommand): Flow<BarsEvent> = when (command) {
        is BarsCommand.GetRemoteBarsConfig -> actorFlow {
            val config = barsConfigRepository.getBarsConfig().getOrThrow()
            val js = barsExtractJsRepository.getExtractJs().getOrThrow()
            delay(CONFIG_DELAY)
            config to js
        }.mapEvents(
            eventMapper = { (config, js) -> Internal.GetRemoteBarsConfigSuccess(config, js) },
            errorMapper = Internal::GetRemoteBarsConfigFailure,
        )

        is BarsCommand.ObserveBars -> barsUserInfoRepository.observeBarsUserInfo()
            .mapEvents(Internal::ObserveBarsSuccess)

        is BarsCommand.SetLatestLoadedUrl -> actorFlow {
            barsUserInfoRepository.latestLoadedUrl = command.latestLoadedUrl.orEmpty()
        }.mapEvents()

        is BarsCommand.GetLatestLoadedUrl -> actorFlow {
            barsUserInfoRepository.latestLoadedUrl.takeIf { it.isNotBlank() }
        }.mapEvents(Internal::GetLatestLoadedUrlSuccess)

        is BarsCommand.PushMarks -> actorFlow {
            barsUserInfoRepository.pushMarksJson(command.marksJson)
        }.mapEvents()

        is BarsCommand.PushStudentName -> actorFlow {
            barsUserInfoRepository.pushStudentName(command.studentName)
        }.mapEvents()

        is BarsCommand.PushStudentGroup -> actorFlow {
            barsUserInfoRepository.pushStudentGroup(command.studentGroup)
        }.mapEvents()

        is BarsCommand.PushStudentRating -> actorFlow {
            barsUserInfoRepository.pushStudentRating(command.ratingJson)
        }.mapEvents()
    }

    private companion object {

        const val CONFIG_DELAY = 16L
    }
}
