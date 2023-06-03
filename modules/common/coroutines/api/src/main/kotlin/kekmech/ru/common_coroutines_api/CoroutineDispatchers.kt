package kekmech.ru.common_coroutines_api

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {

    fun default(): CoroutineDispatcher

    fun main(): CoroutineDispatcher

    fun unconfined(): CoroutineDispatcher

    fun io(): CoroutineDispatcher
}
