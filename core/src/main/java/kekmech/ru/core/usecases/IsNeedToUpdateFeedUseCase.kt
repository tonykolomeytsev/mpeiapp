package kekmech.ru.core.usecases

import androidx.lifecycle.LiveData

interface IsNeedToUpdateFeedUseCase {
    operator fun invoke(): LiveData<Boolean>
}