package kekmech.ru.domain

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.repositories.OldScheduleRepository
import kekmech.ru.core.usecases.LoadOffsetScheduleUseCase

class LoadOffsetScheduleUseCaseImpl constructor(
    private val repository: OldScheduleRepository
) : LoadOffsetScheduleUseCase {

    override operator fun invoke(offset: Int, refresh: Boolean): List<CoupleNative> {
        return repository.getOffsetCouples(offset, refresh)
    }
}