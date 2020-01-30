package kekmech.ru.core.usecases

interface IsSchedulesEmptyUseCase {
    suspend operator fun invoke(): Boolean
}