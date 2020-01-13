package kekmech.ru.core.usecases

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.Schedule

interface GetTimetableScheduleLiveDataUseCase {
    operator fun invoke(): LiveData<Schedule>
}