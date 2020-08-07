package kekmech.ru.common_mvi

/**
 * Represents result of reduce function
 */
data class Result<State, Effect, Action>(
    val state: State,
    val effects: List<Effect>,
    val actions: List<Action>
) {

    constructor(
        state: State,
        effect: Effect? = null,
        action: Action? = null
    ) : this(
        state,
        effect?.let(::listOf) ?: emptyList(),
        action?.let(::listOf) ?: emptyList()
    )
}