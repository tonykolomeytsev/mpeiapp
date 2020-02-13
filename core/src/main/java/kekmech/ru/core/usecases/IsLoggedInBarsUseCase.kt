package kekmech.ru.core.usecases

import androidx.lifecycle.LiveData

interface IsLoggedInBarsUseCase {
    operator fun invoke(): LiveData<Boolean>
}