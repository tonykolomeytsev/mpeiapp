package kekmech.ru.core

interface UseCase<I, R> {
    fun init(i: I)
    fun execute(): R
}