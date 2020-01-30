package kekmech.ru.core.usecases

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.CoupleNative

/**
 * Возвращает пары на завтра (если таковые есть)
 * Возвращает пустой список во время сессии и в недели, номера которых не от 1 до 17
 */
interface GetTomorrowCouplesUseCase {
    operator fun invoke(): LiveData<List<CoupleNative>>
}