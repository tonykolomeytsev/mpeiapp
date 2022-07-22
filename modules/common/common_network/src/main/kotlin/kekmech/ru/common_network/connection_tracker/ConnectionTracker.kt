package kekmech.ru.common_network.connection_tracker

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.NetworkRequest
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class ConnectionTracker(
    private val context: Context,
) {

    private val wifiStateBehavior = BehaviorSubject.create<Boolean>()
    private val cellularStateBehavior = BehaviorSubject.create<Boolean>()

    init {
        startTracking()
    }

    fun observeConnectionState(): Observable<Boolean> =
        Observable
            .combineLatest(wifiStateBehavior, cellularStateBehavior, ::Pair)
            .debounce(NETWORK_STATE_DEBOUNCE, TimeUnit.MILLISECONDS)
            .map { (isWifiEnabled, isCellularEnabled) -> isWifiEnabled or isCellularEnabled }
            .distinctUntilChanged()

    private fun startTracking() {
        wifiStateBehavior.onNext(false)
        cellularStateBehavior.onNext(false)

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.observeNetwork(TRANSPORT_WIFI, wifiStateBehavior::onNext)
        connectivityManager.observeNetwork(TRANSPORT_CELLULAR, cellularStateBehavior::onNext)
    }

    @SuppressLint("MissingPermission")
    private fun ConnectivityManager.observeNetwork(
        transportType: Int,
        onConnectionStateChanged: (Boolean) -> Unit,
    ) {
        val request = NetworkRequest.Builder()
            .addTransportType(transportType)
            .build()
        val callback =
            object : ConnectivityManager.NetworkCallback() {

                override fun onLost(network: Network) {
                    super.onLost(network)
                    onConnectionStateChanged.invoke(false)
                }

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    onConnectionStateChanged.invoke(true)
                }
            }
        registerNetworkCallback(request, callback)
    }

    private companion object {

        const val NETWORK_STATE_DEBOUNCE = 300L
    }
}
