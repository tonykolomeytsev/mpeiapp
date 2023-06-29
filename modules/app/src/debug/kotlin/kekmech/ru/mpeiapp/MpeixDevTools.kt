package kekmech.ru.mpeiapp

import android.content.Context
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.feature_debug_menu.DebugMenuLauncher
import kekmech.ru.mock_server.MockServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

object MpeixDevTools {

    private val mockServerScope by fastLazy {
        val dispatcher = Executors
            .newFixedThreadPool(1)
            .asCoroutineDispatcher()
        CoroutineScope(SupervisorJob() + dispatcher)
    }

    fun init(context: Context, runMockServer: Boolean) {
        if (runMockServer) {
            mockServerScope.launch {
                MockServer(assetManager = context.assets).start()
            }
        }
        DebugMenuLauncher.launch(context)
    }
}
