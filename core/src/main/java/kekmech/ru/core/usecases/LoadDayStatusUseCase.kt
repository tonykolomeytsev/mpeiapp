package kekmech.ru.core.usecases

import kekmech.ru.core.dto.DayStatus

/**
 * Возвращает статус дня, который наступит через [offset] дней после сегодняшнего дня.
 * если в [offset] передать 0, то возвратит статус сегодняшнего дня.
 * Статус в формате [DayStatus]
 */
interface LoadDayStatusUseCase {
    operator fun invoke(offset: Int, refresh: Boolean = false): DayStatus
}