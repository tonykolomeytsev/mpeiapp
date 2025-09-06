package kekmech.ru.lib_coroutines

import kotlinx.coroutines.CoroutineDispatcher

/**
 * An interface that provides access to Coroutine Dispathers in the style recommended by Google.
 * Instead of directly using Dispatchers in an application, this interface should always be
 * injected
 */
public interface CoroutineDispatchers {

    public fun default(): CoroutineDispatcher

    public fun main(): CoroutineDispatcher

    public fun unconfined(): CoroutineDispatcher

    public fun io(): CoroutineDispatcher
}
