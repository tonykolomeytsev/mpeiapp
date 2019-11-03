package kekmech.ru.bars.main.model

interface BarsFragmentModel {
    val isLoggedIn: Boolean

    fun logInUser(login: String, pass: String)
}