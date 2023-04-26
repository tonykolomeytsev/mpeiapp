package kekmech.ru.mpeiapp

import android.content.Context
import kekmech.ru.mock_server.MockServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object MpeixDevTools {

    private val applicationScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    fun init(context: Context, runMockServer: Boolean) {
        if (runMockServer) {
            applicationScope.launch {
                MockServer(assetManager = context.assets).start()
            }
        }
    }
}