package kekmech.ru.common_mvi

import kekmech.ru.common_mvi.util.MappingActor
import io.reactivex.Observable

interface Actor<Action : Any, Event : Any> : MappingActor<Event> {

    fun execute(action: Action): Observable<Event>
}
