package kekmech.ru.ext_okhttp

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Interceptor
import okhttp3.Response

public class NoConnectionInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (isConnected()) {
            return chain.proceed(chain.request())
        } else {
            throw NoInternetConnectionException()
        }
    }

    @SuppressLint("MissingPermission")
    private fun isConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
