package kekmech.ru.domain

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.Building
import kekmech.ru.core.repositories.PlacesRepository
import kekmech.ru.core.usecases.GetBuildingsUseCase

class GetBuildingsUseCaseImpl constructor(
    private val placesRepository: PlacesRepository
) : GetBuildingsUseCase {
    override fun invoke(): LiveData<List<Building>> = placesRepository.buildings
}