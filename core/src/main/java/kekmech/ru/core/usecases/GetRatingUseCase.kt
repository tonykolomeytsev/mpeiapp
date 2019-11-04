package kekmech.ru.core.usecases

import kekmech.ru.core.dto.AcademicScore

interface GetRatingUseCase {
    suspend operator fun invoke(forceRefresh: Boolean = false): AcademicScore?
}