package kekmech.ru.core.usecases

interface LoadNewScheduleUseCase {
    suspend operator fun invoke(groupNum: String): Boolean
}