package kekmech.ru.core.usecases

interface CheckUpdateUseCase {
    operator fun invoke(onUpdateNeededListener: () -> Unit)
}