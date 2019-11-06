package kekmech.ru.core.usecases

interface SaveUserSecretsUseCase {
    operator fun invoke(login: String, pass: String)
}