package kekmech.ru.core.usecases

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.CoupleNative

interface GetTodayCouplesUseCase {
    operator fun invoke(): LiveData<List<CoupleNative>>
}