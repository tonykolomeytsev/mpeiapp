package kekmech.ru.repository.gateways

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson

class StateFactory(val sharedPreferences: SharedPreferences,
                   val gson: Gson) {

    inline fun<reified T: Any> build(autoCachingId: String = ""): State<T> =
        State(
            T::class,
            sharedPreferences,
            gson,
            isAutoCachingEnabled = true,
            autoCachingId = autoCachingId)
}