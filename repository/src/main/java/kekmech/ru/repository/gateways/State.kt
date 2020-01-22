package kekmech.ru.repository.gateways

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass

class State<T : Any>(
    val kClass: KClass<T>,
    val sharedPreferences: SharedPreferences,
    val gson: Gson,
    val liveData: MutableLiveData<T> = MutableLiveData(),
    val isAutoCachingEnabled: Boolean = false,
    val autoCachingId: String = "") {

    init {
        if (isAutoCachingEnabled && autoCachingId.isNotBlank()) updateFromCache()
    }

    operator fun remAssign(value: T) {
        launchMain {
            liveData.value = value
            withContext(IO) {
                val serialized = gson.toJson(value)
                sharedPreferences
                    .edit()
                    .putString(autoCachingId, serialized)
                    .apply()
            }

        }
    }

    private fun updateFromCache() = launchIO {
        val json = sharedPreferences.getString(autoCachingId, "")
        val obj = gson.fromJson(json, kClass.java)
        withContext(Main) {
            liveData.value = obj
        }
    }

    private fun launchMain(block: suspend CoroutineScope.() -> Unit) = GlobalScope.launch(Main, block = block)
    private fun launchIO(block: suspend CoroutineScope.() -> Unit) = GlobalScope.launch(IO, block = block)
}