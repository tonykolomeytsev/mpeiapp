package kekmech.ru.core.usecases

import kekmech.ru.core.dto.AcademicSession

interface GetAcademicSessionUseCase {
    operator fun invoke(): AcademicSession?
}