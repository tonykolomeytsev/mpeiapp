package kekmech.ru.domain

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.Hostel
import kekmech.ru.core.repositories.PlacesRepository
import kekmech.ru.core.usecases.GetHostelsUseCase
import javax.inject.Inject

class GetHostelsUseCaseImpl @Inject constructor(
    private val placesRepository: PlacesRepository
) : GetHostelsUseCase {
    override fun invoke(): LiveData<List<Hostel>> = placesRepository.hostels
}