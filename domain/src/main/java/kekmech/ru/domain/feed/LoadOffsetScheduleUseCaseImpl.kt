package kekmech.ru.domain.feed

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.core.usecases.LoadOffsetScheduleUseCase
import javax.inject.Inject

class LoadOffsetScheduleUseCaseImpl @Inject constructor(
    private val repository: ScheduleRepository
) : LoadOffsetScheduleUseCase {

    override fun execute(offset: Int, refresh: Boolean): List<CoupleNative> {
        return repository.getOffsetCouples(offset, refresh)
    }
}