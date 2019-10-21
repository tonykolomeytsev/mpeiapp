package kekmech.ru.core.usecases

import androidx.lifecycle.LiveData

interface GetGroupNumberUseCase {
    operator fun invoke(): LiveData<String>
}