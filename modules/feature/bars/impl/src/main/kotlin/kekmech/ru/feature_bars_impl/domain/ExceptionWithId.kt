package kekmech.ru.feature_bars_impl.domain

import java.util.UUID

internal class ExceptionWithId(val source: Throwable) : RuntimeException() {
    val uuid: String = UUID.randomUUID().toString()
}
