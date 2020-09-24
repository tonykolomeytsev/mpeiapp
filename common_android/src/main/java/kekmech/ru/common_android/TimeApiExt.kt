package kekmech.ru.common_android

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

fun moscowLocalTime(): LocalTime {
    return LocalTime.now(ZoneId.of("Europe/Moscow"))
}

fun moscowLocalDate(): LocalDate {
    return LocalDate.now(ZoneId.of("Europe/Moscow"))
}