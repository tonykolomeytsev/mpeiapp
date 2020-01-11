package kekmech.ru.domain

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.AcademicScore
import kekmech.ru.core.repositories.BarsRepository
import kekmech.ru.core.usecases.GetRatingLiveDataUseCase

class GetRatingLiveDataUseCaseImpl(
    private val barsRepository: BarsRepository
) : GetRatingLiveDataUseCase {
    override fun invoke() = barsRepository.score
}