package kekmech.ru.feature_bars_impl.presentation.screen.login.layout

import io.github.tonykolomeytsev.mpeix.sdk.LoginError
import io.github.tonykolomeytsev.mpeix.sdk.SdkError

/** The reason shown after [MAX_CODE_ATTEMPTS] wrong codes: the site has no limit, the app sets it. */
internal const val TOO_MANY_CODES_MESSAGE = "Слишком много неверных кодов. Попробуйте войти ещё раз"

internal fun LoginError.text(): String =
    when (this) {
        is LoginError.InvalidCredentials -> "Неверный логин или пароль"
        is LoginError.InvalidCode -> "Неверный код"
        is LoginError.CodeExpired -> "Код устарел. Отправьте новый код"
        is LoginError.AttemptExpired -> "Время на вход истекло. Введите пароль ещё раз"
        is LoginError.Rejected -> message ?: "Сайт БАРС отклонил вход"
        is LoginError.Sdk -> error.text()
        else -> "Произошла ошибка"
    }

private fun SdkError.text(): String {
    val message = when (this) {
        is SdkError.NoConnection -> "Нет подключения к интернету"
        is SdkError.Timeout -> "Сайт БАРС не отвечает"
        is SdkError.SiteUnavailable -> "Сайт БАРС недоступен"
        is SdkError.SiteChanged -> "Сайт БАРС изменился. Мы уже знаем об этом и скоро всё починим"
        else -> "Произошла ошибка"
    }
    return "$message. Код ошибки: ${diagnosticId.value}"
}
