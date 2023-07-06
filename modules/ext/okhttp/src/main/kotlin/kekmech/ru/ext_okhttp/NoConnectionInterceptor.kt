package kekmech.ru.ext_okhttp

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response

class NoConnectionInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (isConnected()) {
            return chain.proceed(chain.request())
        } else {
            throw NoInternetConnectionException()
        }
    }

    @SuppressLint("MissingPermission")
    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
}
