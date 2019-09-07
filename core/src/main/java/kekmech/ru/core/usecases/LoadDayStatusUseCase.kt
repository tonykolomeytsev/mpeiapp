package kekmech.ru.core.usecases

import kekmech.ru.core.dto.DayStatus

interface LoadDayStatusUseCase {
    fun execute(offset: Int, refresh: Boolean = false): DayStatus
}