package kekmech.ru.core.usecases

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.AcademicScore

interface GetRatingLiveDataUseCase {
    operator fun invoke(): LiveData<AcademicScore>
}