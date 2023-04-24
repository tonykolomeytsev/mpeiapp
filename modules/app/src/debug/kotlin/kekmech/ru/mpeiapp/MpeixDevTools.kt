package kekmech.ru.mpeiapp

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object MpeixDevTools {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun init(context: Context) {
        applicationScope.launch {
            MockServerWrapper(context).start()
        }
    }
}