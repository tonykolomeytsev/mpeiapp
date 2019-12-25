package kekmech.ru.domain

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.Food
import kekmech.ru.core.repositories.PlacesRepository
import kekmech.ru.core.usecases.GetFoodsUseCase

class GetFoodsUseCaseImpl constructor(
    private val placesRepository: PlacesRepository
) : GetFoodsUseCase {
    override fun invoke(): LiveData<List<Food>> = placesRepository.foods
}