package kekmech.ru.core.usecases

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Time

/**
 * Возвращает список пар для дня, который наступит через [offset] дней после сегодняшнего дня.
 * Если передать в [offset] ноль, то возвратит пары на сегодня.
 */
interface LoadOffsetScheduleUseCase {
    operator fun invoke(offset: Int, refresh: Boolean): List<CoupleNative>
}