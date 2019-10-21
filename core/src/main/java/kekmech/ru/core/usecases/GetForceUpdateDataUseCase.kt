package kekmech.ru.core.usecases

interface GetForceUpdateDataUseCase {
    operator fun invoke(): Pair<String, String>
}