package kekmech.ru.common_mvi

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

interface Feature<State : Any, Event : Any, Effect : Any> : Consumer<Event>, Disposable {
    val states: Observable<State>
    val effects: Observable<Effect>
    fun start(): Feature<State, Event, Effect>
}