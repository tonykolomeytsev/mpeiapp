package kekmech.ru.domain

import kekmech.ru.core.dto.AcademicDiscipline
import kekmech.ru.core.repositories.BarsRepository
import kekmech.ru.core.usecases.GetDetailsDisciplineUseCase
import kekmech.ru.core.usecases.SetDetailsDisciplineUseCase
import javax.inject.Inject

class GetDetailsDisciplineUseCaseImpl @Inject constructor(
    private val barsRepository: BarsRepository
): GetDetailsDisciplineUseCase {
    override fun invoke(): AcademicDiscipline? {
        return barsRepository.currentAcademicDiscipline
    }
}