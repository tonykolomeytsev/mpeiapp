package kekmech.ru.core

interface Router {
    fun navigate(fragmentId: Screens)
    fun popBackStack()
}