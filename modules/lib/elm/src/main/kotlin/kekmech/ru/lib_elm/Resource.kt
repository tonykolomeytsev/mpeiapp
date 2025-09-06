package kekmech.ru.lib_elm

//import androidx.compose.runtime.Immutable

//@Immutable
public sealed class Resource<out T : Any> private constructor() {

    public open val value: T? = null
    public open val error: Throwable? = null

    public val isLoading: Boolean get() = this is Loading
    public val isError: Boolean get() = this is Error
    public val isData: Boolean get() = this is Data

    public data object Loading : Resource<Nothing>()
    public data class Data<T : Any>(override val value: T) : Resource<T>()
    public data class Error(override val error: Throwable) : Resource<Nothing>()
}

public inline fun <reified T : Any> Any.toResource(): Resource<T> =
    when (this) {
        is Throwable -> Resource.Error(this)
        is T -> Resource.Data(this)
        else -> error("Cannot convert $this to Resource<T>")
    }

public fun <T : Any> Resource<T>.toLoadingIfError(): Resource<T> =
    Resource.Loading.takeIf { isError } ?: this

@Suppress("UNCHECKED_CAST")
public fun <T : Any, R : Any> Collection<Resource<T>>.merge(
    mergeRule: (Collection<T>) -> R = { it as R },
): Resource<R> {
    var isLoading = false
    for (e in this) {
        when (e) {
            is Resource.Error -> return Resource.Error(e.error)
            is Resource.Loading -> isLoading = true
            is Resource.Data -> { /* no-op */
            }
        }
    }
    if (isLoading) return Resource.Loading
    return Resource.Data(mergeRule(this.map { it.value!! }))
}

public inline fun <reified T : Any> Resource<T>.map(transform: (T) -> T): Resource<T> =
    when (this) {
        is Resource.Data<T> -> Resource.Data(transform(this.value))
        else -> this
    }

public inline fun <reified A : Any, reified B : Any> Resource<A>.zip(
    another: Resource<B>,
): Resource<Pair<A, B>> =
    when {
        this is Resource.Data && another is Resource.Data ->
            (this.value to another.value).toResource()
        this is Resource.Error -> this.error.toResource()
        another is Resource.Error -> another.error.toResource()
        else -> Resource.Loading
    }
