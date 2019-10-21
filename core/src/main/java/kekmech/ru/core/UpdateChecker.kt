package kekmech.ru.core

interface UpdateChecker {
    fun check(onUpdateNeededListener: (url: String, description: String) -> Unit)
}