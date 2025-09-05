package kekmech.ru.feature_bars.screen.main.elm

import kekmech.ru.common_elm.actorEmptyFlow
import kekmech.ru.common_elm.actorFlow
import kekmech.ru.domain_bars.BarsRepository
import kekmech.ru.feature_bars.screen.main.elm.BarsEvent.Internal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.rx3.asFlow
import kotlinx.coroutines.rx3.await
import money.vivid.elmslie.core.store.Actor
import kotlin.time.toKotlinDuration

internal class BarsActor(
    private val barsRepository: BarsRepository,
) : Actor<BarsCommand, BarsEvent>() {

    override fun execute(command: BarsCommand): Flow<BarsEvent> = when (command) {
        is BarsCommand.GetRemoteBarsConfig -> actorFlow {
            delay(java.time.Duration.ofMillis(CONFIG_DELAY).toKotlinDuration())
            val config = barsRepository.getRemoteBarsConfig()
            val js = barsRepository.getExtractJs()
            config.await() to js.await()
        }.mapEvents(
            eventMapper = { (config, js) -> Internal.GetRemoteBarsConfigSuccess(config, js) },
            errorMapper = Internal::GetRemoteBarsConfigFailure,
        )

        is BarsCommand.ObserveBars -> barsRepository.observeUserBars()
            .asFlow()
            .mapEvents(Internal::ObserveBarsSuccess)

        is BarsCommand.SetLatestLoadedUrl -> actorEmptyFlow {
            barsRepository.latestLoadedUrl = command.latestLoadedUrl.orEmpty()
        }

        is BarsCommand.GetLatestLoadedUrl -> actorFlow {
            barsRepository.latestLoadedUrl
        }.mapEvents(
            eventMapper = { url -> Internal.GetLatestLoadedUrlSuccess(url.takeIf { it.isNotBlank() }) }
        )

        is BarsCommand.PushMarks -> actorEmptyFlow {
            barsRepository.pushMarksJson(command.marksJson)
                .onErrorComplete()
                .await()
        }

        is BarsCommand.PushStudentName -> actorEmptyFlow {
            barsRepository.pushStudentName(command.studentName)
                .onErrorComplete()
                .await()
        }

        is BarsCommand.PushStudentGroup -> actorEmptyFlow {
            barsRepository.pushStudentGroup(command.studentGroup)
                .onErrorComplete().await()
        }

        is BarsCommand.PushStudentRating -> actorEmptyFlow {
            barsRepository.pushStudentRating(command.ratingJson).await()
        }
    }

    private companion object {

        const val CONFIG_DELAY = 16L
    }
}
