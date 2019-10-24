package kekmech.ru.core.usecases

interface ChangeCurrentScheduleIdUseCase {
    operator fun invoke(newCurrentId: Int)
}