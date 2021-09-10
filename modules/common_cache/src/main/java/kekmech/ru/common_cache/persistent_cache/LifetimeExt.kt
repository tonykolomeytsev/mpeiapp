package kekmech.ru.common_cache.persistent_cache

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun Long.toLocalDateTime(): LocalDateTime =
    Instant
        .ofEpochMilli(this)
        .let { LocalDateTime.ofInstant(it, ZoneId.of("Europe/Moscow")) }
