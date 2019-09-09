package kekmech.ru.core

interface Router {
    fun navigateTo(fragmentId: String)
    fun replaceScreen(fragmentId: String)
    fun popBackStack()
}