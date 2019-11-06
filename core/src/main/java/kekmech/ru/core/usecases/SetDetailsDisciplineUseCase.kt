package kekmech.ru.core.usecases

import kekmech.ru.core.dto.AcademicDiscipline
import kekmech.ru.core.dto.AcademicScore

interface SetDetailsDisciplineUseCase {
    operator fun invoke(academicDiscipline: AcademicDiscipline)
}