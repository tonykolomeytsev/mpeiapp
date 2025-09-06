package kekmech.ru.lib_elm

import timber.log.Timber
import money.vivid.elmslie.core.logger.strategy.LogStrategy

public object TimberLogger {

    public val E: LogStrategy = invoke(Timber::e)
    public val D: LogStrategy = invoke(Timber::d)

    private fun invoke(
        log: (Throwable?, String) -> Unit
    ) = LogStrategy { _, _, message, throwable -> log(throwable, message) }
}
