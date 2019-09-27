package kekmech.ru.core.usecases

import kekmech.ru.core.dto.CoupleNative

/**
 * Возвращает список пар на конкретный день, конкретной недели.
 * В случае, если в заданный день или неделю нет пар (например, неделя не в диапазоне 1..18),
 * будет возвращен пустой список
 */
interface GetTimetableScheduleUseCase {
    operator fun invoke(dayOfWeek: Int, weekNum: Int): List<CoupleNative>
}