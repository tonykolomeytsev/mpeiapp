package kekmech.ru.domain

import kekmech.ru.core.dto.AcademicDiscipline
import kekmech.ru.core.repositories.BarsRepository
import kekmech.ru.core.usecases.GetDetailsDisciplineUseCase

class GetDetailsDisciplineUseCaseImpl constructor(
    private val barsRepository: BarsRepository
): GetDetailsDisciplineUseCase {
    override fun invoke(): AcademicDiscipline? {
        return barsRepository.currentAcademicDiscipline
    }
}