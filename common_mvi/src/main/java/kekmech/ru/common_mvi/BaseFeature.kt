package kekmech.ru.common_mvi

import kekmech.ru.common_mvi.util.DisposableDelegate
import kekmech.ru.common_mvi.util.DisposableDelegateImpl
import kekmech.ru.common_mvi.util.EffectsBuffer
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers.io
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class BaseFeature<State : Any, Event : Any, Effect : Any, Action : Any>(
    initialState: State,
    private val reducer: BaseReducer<State, Event, Effect, Action>,
    private val actor: Actor<Action, Event>
) : Feature<State, Event, Effect>,
    DisposableDelegate by DisposableDelegateImpl() {

    private val scheduler = Schedulers.newThread()

    // We can't use subject to store state to keep it synchronized with children
    private val stateReferenceLock = Any()
    // Only to emit states to subscribers
    private val statesInternal = BehaviorSubject.createDefault(initialState)
    private val effectsInternal = PublishSubject.create<Effect>()
    private val effectsBuffer = EffectsBuffer(effectsInternal)
    private val eventsInternal = PublishSubject.create<Event>()
    private val commandsInternal = PublishSubject.create<Action>()

    override val effects: Observable<Effect> = effectsBuffer.getBufferedObservable()
    override val states: Observable<State> = statesInternal.distinctUntilChanged()
    override fun accept(event: Event) = eventsInternal.onNext(event)

    override fun start(): Feature<State, Event, Effect> {
        effectsBuffer.init().bind()

        eventsInternal
            .observeOn(scheduler)
            .flatMap { event ->
                val (effects, commands) = synchronized(stateReferenceLock) {
                    val state = statesInternal.value!!
                    val result = reducer.reduce(event, state)
                    statesInternal.onNext(result.state)
                    result.effects to result.actions
                }
                effects.forEach(effectsInternal::onNext)
                Observable.fromIterable(commands)
            }
            .subscribe(commandsInternal::onNext)
            .bind()

        commandsInternal
            .flatMap { actor.execute(it).subscribeOn(io()) }
            .subscribe(eventsInternal::onNext) {
                error(Exception("You must handle all errors inside actor $actor", it))
            }
            .bind()

        return this
    }
}