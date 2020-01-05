package kekmech.ru.domain

import kekmech.ru.core.dto.Time
import kekmech.ru.core.usecases.IsSemesterStartUseCase
import java.util.*

class IsSemesterStartUseCaseImpl : IsSemesterStartUseCase {
    override fun invoke(): Boolean {
        val today = Time()
        return when (today.month) {
            Calendar.FEBRUARY, Calendar.SEPTEMBER -> true
            else -> false
        }
    }
}