package kekmech.ru.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

val Dispatchers.ASYNCIO by lazy { Executors.newFixedThreadPool(2).asCoroutineDispatcher() }