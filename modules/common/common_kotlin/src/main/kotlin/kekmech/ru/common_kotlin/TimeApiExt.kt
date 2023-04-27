package kekmech.ru.common_kotlin

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

fun moscowLocalTime(): LocalTime {
    return LocalTime.now(ZoneId.of("Europe/Moscow"))
}

fun moscowLocalDate(): LocalDate {
    return LocalDate.now(ZoneId.of("Europe/Moscow"))
}

fun moscowLocalDateTime(): LocalDateTime = LocalDateTime.of(moscowLocalDate(), moscowLocalTime())
