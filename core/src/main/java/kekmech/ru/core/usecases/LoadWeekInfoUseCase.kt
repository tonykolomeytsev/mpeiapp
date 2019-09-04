package kekmech.ru.core.usecases

@Deprecated("WeekInfo wil be deleted/ Use Status instead")
interface LoadWeekInfoUseCase {
    /**
     * Имеется ввиду учебная неделя, а не календарная
     */
    fun getCurrentWeekNumber(): Int
}