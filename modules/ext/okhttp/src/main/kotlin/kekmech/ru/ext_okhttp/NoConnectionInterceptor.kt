package kekmech.ru.ext_okhttp

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Reports a failed request as [NoInternetConnectionException] when the device is offline.
 *
 * Connectivity is checked only after a request fails, never before: a pre-check on VALIDATED
 * blocked every request under a system-wide VPN, whose network may not report VALIDATED
 * even though requests through it work.
 */
public class NoConnectionInterceptor internal constructor(
    private val isOffline: () -> Boolean,
) : Interceptor {

    public constructor(context: Context) : this(isOffline = { context.isOffline() })

    override fun intercept(chain: Interceptor.Chain): Response =
        try {
            chain.proceed(chain.request())
        } catch (e: IOException) {
            val offline = e !is NoInternetConnectionException && !chain.call().isCanceled() && isOffline()
            throw if (offline) NoInternetConnectionException(cause = e) else e
        }
}

@SuppressLint("MissingPermission")
private fun Context.isOffline(): Boolean {
    val cm = getSystemService(ConnectivityManager::class.java) ?: return false
    val network = cm.activeNetwork ?: return true
    val caps = cm.getNetworkCapabilities(network) ?: return true
    if (!caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) return true
    // A VPN's VALIDATED says nothing reliable, so under a VPN the request's own error is kept.
    // Without one, an unvalidated network (captive portal, no uplink) means no internet.
    return !caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN) &&
        !caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}
