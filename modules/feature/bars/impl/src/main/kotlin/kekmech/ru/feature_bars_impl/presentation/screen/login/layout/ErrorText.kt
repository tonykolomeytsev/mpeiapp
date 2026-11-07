package kekmech.ru.feature_bars_impl.presentation.screen.login.layout

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.tonykolomeytsev.mpeix.sdk.LoginError
import io.github.tonykolomeytsev.mpeix.sdk.SdkError
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
internal fun ErrorText(error: LoginError, modifier: Modifier = Modifier) {
    Text(
        text = error.text(),
        style = MpeixTheme.typography.bodyMedium,
        color = MpeixTheme.palette.error,
        modifier = modifier,
    )
}

internal fun LoginError.text(): String =
    when (this) {
        is LoginError.InvalidCredentials -> "Неверный логин или пароль"
        is LoginError.InvalidCode -> buildString {
            append("Неверный код")
            if (attemptsLeft != null) append(". Осталось попыток: $attemptsLeft")
        }

        is LoginError.CodeExpired -> "Код устарел. Отправьте новый код"
        is LoginError.AttemptExpired -> "Время на вход истекло. Введите пароль ещё раз"
        is LoginError.TooManyAttempts -> "Слишком много попыток. Попробуйте позже"
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
