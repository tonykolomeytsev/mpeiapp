package kekmech.ru.lib_elm

import timber.log.Timber
import vivid.money.elmslie.core.logger.strategy.LogStrategy

object TimberLogger {

    val E = invoke(Timber::e)
    val D = invoke(Timber::d)

    private fun invoke(
        log: (Throwable?, String) -> Unit
    ) = LogStrategy { _, message, throwable -> log(throwable, message) }
}
