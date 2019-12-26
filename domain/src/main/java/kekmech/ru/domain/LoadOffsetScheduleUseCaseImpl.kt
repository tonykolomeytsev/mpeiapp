package kekmech.ru.domain

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.LoadOffsetScheduleUseCase

class LoadOffsetScheduleUseCaseImpl constructor(
    private val repository: ScheduleRepository
) : LoadOffsetScheduleUseCase {

    override operator fun invoke(offset: Int, refresh: Boolean): List<CoupleNative> {
        return repository.getOffsetCouples(offset, refresh)
    }
}