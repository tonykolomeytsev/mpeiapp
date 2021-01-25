package kekmech.ru.common_mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.util.MappingActor

interface Actor<Action : Any, Event : Any> : MappingActor<Event> {

    fun execute(action: Action): Observable<Event>
}

class NoOpActor<Action : Any, Event : Any> : Actor<Action, Event> {

    override fun execute(action: Action): Observable<Event> = Observable.never()
}
