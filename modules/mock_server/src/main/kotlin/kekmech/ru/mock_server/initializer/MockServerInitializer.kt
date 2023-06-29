package kekmech.ru.mock_server.initializer

import android.content.Context
import kekmech.ru.common_app_lifecycle.AppLifecycleObserver
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.mock_server.MockServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

internal class MockServerInitializer : AppLifecycleObserver {

    private val mockServerScope by fastLazy {
        val dispatcher = Executors
            .newFixedThreadPool(1)
            .asCoroutineDispatcher()
        CoroutineScope(SupervisorJob() + dispatcher)
    }

    override fun onCreate(context: Context) {
        mockServerScope.launch {
            MockServer(assetManager = context.assets).start()
        }
    }
}
