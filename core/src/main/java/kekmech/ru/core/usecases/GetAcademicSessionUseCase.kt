package kekmech.ru.core.usecases

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.AcademicSession

interface GetAcademicSessionUseCase {
    operator fun invoke(): LiveData<AcademicSession>
}