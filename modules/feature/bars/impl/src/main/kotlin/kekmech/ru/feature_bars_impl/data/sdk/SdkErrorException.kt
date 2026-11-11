package kekmech.ru.feature_bars_impl.data.sdk

import io.github.tonykolomeytsev.mpeix.sdk.SdkError

/**
 * An [SdkError] carried where the presentation layer expects a [Throwable].
 * The SDK has already reported it through its event sink, so it is not recorded again.
 */
internal class SdkErrorException(val error: SdkError) :
    RuntimeException("${error::class.simpleName} (diagnostic ${error.diagnosticId})")
