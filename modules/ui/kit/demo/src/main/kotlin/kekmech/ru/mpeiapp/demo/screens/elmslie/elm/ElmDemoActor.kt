package kekmech.ru.mpeiapp.demo.screens.elmslie.elm

import kekmech.ru.mpeiapp.demo.screens.elmslie.elm.ElmDemoCommand
import kekmech.ru.mpeiapp.demo.screens.elmslie.elm.ElmDemoEvent
import kekmech.ru.mpeiapp.demo.screens.elmslie.elm.ElmDemoEvent.Internal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import money.vivid.elmslie.core.store.Actor
import java.util.UUID
import kotlin.random.Random
import kekmech.ru.mpeiapp.demo.screens.elmslie.elm.ElmDemoCommand as Command
import kekmech.ru.mpeiapp.demo.screens.elmslie.elm.ElmDemoEvent as Event

internal class ElmDemoActor : Actor<Command, Event>() {

    override fun execute(command: ElmDemoCommand): Flow<ElmDemoEvent> =
        when (command) {
            is ElmDemoCommand.LoadNewResource -> flow {
                @Suppress("MagicNumber")
                delay(1000L)
                if (Random.nextBoolean()) {
                    emit(UUID.randomUUID().toString())
                } else {
                    throw IllegalStateException("Oh no!")
                }
            }.mapEvents(Internal::LoadNewResourceSuccess, Internal::LoadNewResourceFailure)
        }
}
