package kekmech.ru.core.usecases

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.Building

interface GetBuildingsUseCase {
    operator fun invoke(): LiveData<List<Building>>
}