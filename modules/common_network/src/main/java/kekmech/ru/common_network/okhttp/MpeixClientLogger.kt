package kekmech.ru.common_network.okhttp

import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

object MpeixClientLogger : HttpLoggingInterceptor.Logger {

    override fun log(message: String) {
        Timber.d(message)
    }
}