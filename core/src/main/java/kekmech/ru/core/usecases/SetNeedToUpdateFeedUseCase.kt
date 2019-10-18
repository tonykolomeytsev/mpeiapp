package kekmech.ru.core.usecases

interface SetNeedToUpdateFeedUseCase {
    operator fun invoke(update: Boolean)
}