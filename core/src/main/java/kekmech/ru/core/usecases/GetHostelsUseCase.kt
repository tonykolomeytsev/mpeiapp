package kekmech.ru.core.usecases

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.Hostel

interface GetHostelsUseCase {
    operator fun invoke(): LiveData<List<Hostel>>
}