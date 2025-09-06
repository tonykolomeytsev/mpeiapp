package kekmech.ru.ext_kotlin

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

public fun moscowLocalTime(): LocalTime {
    return LocalTime.now(ZoneId.of("Europe/Moscow"))
}

public fun moscowLocalDate(): LocalDate {
    return LocalDate.now(ZoneId.of("Europe/Moscow"))
}

public fun moscowLocalDateTime(): LocalDateTime = LocalDateTime.of(moscowLocalDate(), moscowLocalTime())
