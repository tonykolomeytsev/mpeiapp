package kekmech.ru.mpeiapp

import android.content.Context
import kekmech.ru.mock_server.MockServer
import kekmech.ru.mock_server.MockServerLogger
import timber.log.Timber

internal class MockServerWrapper(context: Context) {

    private val logger: MockServerLogger =
        TimberMockServerLogger()

    private val mockServer =
        MockServer(
            assetManager = context.assets,
            logger = logger,
        )

    fun start() = mockServer.start()

    private class TimberMockServerLogger : MockServerLogger {

        override fun i(msg: Any) {
            when (msg) {
                is Throwable -> Timber.i(msg)
                else -> Timber.i(msg.toString())
            }
        }

        override fun e(msg: Any) {
            when (msg) {
                is Throwable -> Timber.d(msg)
                else -> Timber.d(msg.toString())
            }
        }

        override fun d(msg: Any) {
            when (msg) {
                is Throwable -> Timber.d(msg)
                else -> Timber.d(msg.toString())
            }
        }
    }
}