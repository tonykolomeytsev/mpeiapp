package kekmech.ru.mock_server

import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.random.nextInt

private val random = Random(System.currentTimeMillis())

@Suppress("MagicNumber")
internal suspend fun randomResponseDelay() {
    delay(random.nextInt(50..700).toLong())
}
