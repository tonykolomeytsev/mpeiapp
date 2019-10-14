package kekmech.ru.core.usecases

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.Food

interface GetFoodsUseCase {
    operator fun invoke(): LiveData<List<Food>>
}