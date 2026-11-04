package kekmech.ru.ui_kit_states

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import kekmech.ru.ext_okhttp.NoInternetConnectionException
import kekmech.ru.ui_kit_buttons.button.Button
import kekmech.ru.ui_kit_buttons.button.ButtonPalette
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_theme.theme.MpeixTheme
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import kekmech.ru.res_images.R.drawable as Images
import kekmech.ru.res_strings.R.string as Strings

private data class ErrorStateContent(
    @StringRes val title: Int,
    @StringRes val description: Int,
    val descriptionArg: String? = null,
    @DrawableRes val illustration: Int,
)

/**
 * # ErrorState
 *
 * Compose version of `ErrorStateItem` from coreui: illustration, title and description
 * picked by the type of the [throwable], and the reload button.
 *
 * Usage example:
 * ```kotlin
 * ErrorState(
 *     throwable = state.loadingError,
 *     onReloadClick = { onAccept(Ui.Action.Reload) },
 * )
 * ```
 *
 * @param throwable the error to describe
 * @param onReloadClick called when the reload button is clicked
 * @param modifier [Modifier] to be applied to the error state
 */
@Composable
public fun ErrorState(
    throwable: Throwable,
    onReloadClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val content = getErrorStateContent(throwable)
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(content.illustration),
            contentDescription = null,
            modifier = Modifier.height(48.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(content.title),
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MpeixTheme.typography.titleLarge,
            color = MpeixTheme.palette.onSurface,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(content.description, content.descriptionArg.orEmpty()),
            modifier = Modifier.padding(horizontal = 32.dp),
            style = MpeixTheme.typography.labelMedium,
            color = MpeixTheme.palette.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(16.dp))
        Button(
            text = stringResource(Strings.common_error_reload),
            onClick = onReloadClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            palette = ButtonPalette.Secondary,
        )
    }
}

@Suppress("MagicNumber")
private fun getErrorStateContent(throwable: Throwable?): ErrorStateContent =
    when (throwable) {
        is NoInternetConnectionException,
        is UnknownHostException,
        -> ErrorStateContent(
            title = Strings.common_error_no_internet_title,
            description = Strings.common_error_no_internet_description,
            illustration = Images.ill_no_internet_error,
        )

        is HttpException ->
            ErrorStateContent(
                title = Strings.common_error_bad_response_title,
                description = Strings.common_error_bad_response_description,
                descriptionArg = throwable.code().toString(),
                illustration = Images.ill_bad_response_error,
            )
                .takeIf { throwable.code() in 300 until 600 }
                ?: getErrorStateContent(null)

        is SSLHandshakeException -> ErrorStateContent(
            title = Strings.common_error_handshake_title,
            description = Strings.common_error_handshake_description,
            illustration = Images.ill_handshake_error,
        )

        else -> ErrorStateContent(
            title = Strings.common_error_view_title,
            description = Strings.common_error_view_description,
            illustration = Images.ill_view_error,
        )
    }

/**
 * One throwable for every branch of [getErrorStateContent]
 */
private class ErrorStatePreviewProvider : PreviewParameterProvider<Throwable> {

    override val values: Sequence<Throwable> = sequenceOf(
        NoInternetConnectionException(),
        UnknownHostException(),
        httpException(code = 502),
        // Codes outside of 300..599 fall back to the generic error,
        // and Response.error() accepts only codes >= 400
        httpException(code = 600),
        SSLHandshakeException("Handshake failed"),
        IllegalStateException(),
    )

    private fun httpException(code: Int): HttpException =
        HttpException(Response.error<Unit>(code, "".toResponseBody()))
}

@Composable
@PreviewLightDark
private fun ErrorStatePreview(
    @PreviewParameter(ErrorStatePreviewProvider::class) throwable: Throwable,
) {
    MpeixPreview {
        ErrorState(
            throwable = throwable,
            onReloadClick = { /* no-op */ },
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(vertical = 24.dp),
        )
    }
}
