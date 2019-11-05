package kekmech.ru.domain

import kekmech.ru.core.dto.AcademicDiscipline
import kekmech.ru.core.repositories.BarsRepository
import kekmech.ru.core.usecases.SetDetailsDisciplineUseCase
import javax.inject.Inject

class SetDetailsDisciplineUseCaseImpl @Inject constructor(
    private val barsRepository: BarsRepository
): SetDetailsDisciplineUseCase {
    override fun invoke(academicDiscipline: AcademicDiscipline) {
        barsRepository.currentAcademicDiscipline = academicDiscipline
    }
}