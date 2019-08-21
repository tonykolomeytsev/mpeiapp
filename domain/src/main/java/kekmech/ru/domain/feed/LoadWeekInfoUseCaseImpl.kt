package kekmech.ru.domain.feed

import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.LoadWeekInfoUseCase
import javax.inject.Inject

class LoadWeekInfoUseCaseImpl @Inject constructor(
    val scheduleRepository: ScheduleRepository
) : LoadWeekInfoUseCase {
    /**
     * Имеется ввиду учебная неделя, а не календарная
     */
    override fun getCurrentWeekNumber(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}