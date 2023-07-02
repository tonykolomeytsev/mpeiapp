package kekmech.ru.library_coroutines

import kotlinx.coroutines.CoroutineDispatcher

/**
 * An interface that provides access to Coroutine Dispathers in the style recommended by Google.
 * Instead of directly using Dispatchers in an application, this interface should always be
 * injected
 */
interface CoroutineDispatchers {

    fun default(): CoroutineDispatcher

    fun main(): CoroutineDispatcher

    fun unconfined(): CoroutineDispatcher

    fun io(): CoroutineDispatcher
}
