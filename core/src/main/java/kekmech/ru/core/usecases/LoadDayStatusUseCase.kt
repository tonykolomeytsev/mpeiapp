package kekmech.ru.core.usecases

import kekmech.ru.core.dto.DayStatus

interface LoadDayStatusUseCase {
    operator fun invoke(offset: Int, refresh: Boolean = false): DayStatus
}