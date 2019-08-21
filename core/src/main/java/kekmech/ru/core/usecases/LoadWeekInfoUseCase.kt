package kekmech.ru.core.usecases

interface LoadWeekInfoUseCase {
    /**
     * Имеется ввиду учебная неделя, а не календарная
     */
    fun getCurrentWeekNumber(): Int
}