package kekmech.ru.common_mvi

interface BaseReducer<State : Any, Event : Any, Effect : Any, Action : Any> {

    fun reduce(event: Event, state: State): Result<State, Effect, Action>
}
