package kekmech.ru.mock_server.ext

import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.random.nextInt

private val random = Random(System.currentTimeMillis())

internal suspend fun randomResponseDelay() {
    delay(random.nextInt(50..300).toLong())
}