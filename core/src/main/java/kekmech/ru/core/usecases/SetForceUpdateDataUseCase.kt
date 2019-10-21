package kekmech.ru.core.usecases

interface SetForceUpdateDataUseCase {
    operator fun invoke(url: String, description: String)
}