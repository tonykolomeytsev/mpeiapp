package kekmech.ru.core.usecases

interface ChangeCurrentScheduleUseCase {
    suspend operator fun invoke(groupNumber: String)
}