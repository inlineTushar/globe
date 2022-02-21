package com.globe.data.extension

import android.content.Context
import android.net.*
import android.net.ConnectivityManager.NetworkCallback
import android.os.Build
import com.globe.data.extension.NetworkStatus.Available
import com.globe.data.extension.NetworkStatus.Unavailable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

class NetworkCheck(context: Context) {

    private val cm =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val isConnected: Boolean
        get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val cap = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
                return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            } else {
                val networks = cm.allNetworks
                for (n in networks) {
                    val nInfo: NetworkInfo? = cm.getNetworkInfo(n)
                    if (nInfo != null && nInfo.isConnected) return true
                }
                return false
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun changes() = callbackFlow {
        val networkStatusCallback = object : NetworkCallback() {
            override fun onUnavailable() {
                trySend(Unavailable)
            }

            override fun onAvailable(network: Network) {
                trySend(Available)
            }

            override fun onLost(network: Network) {
                trySend(Unavailable)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        cm.registerNetworkCallback(request, networkStatusCallback)
        awaitClose { cm.unregisterNetworkCallback(networkStatusCallback) }
    }.onStart { emit(if (isConnected) Available else Unavailable) }
}

sealed class NetworkStatus {
    object Available : NetworkStatus()
    object Unavailable : NetworkStatus()
}