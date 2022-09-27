package com.medium.client.common.wrappers.connectivity

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

interface NetworkConnectivityManager {
    fun observeNetworkStatus(): Flow<NetworkStatus>
}

class NetworkConnectivityManagerImpl @Inject constructor(
    private val connectivityManager: ConnectivityManager
) : NetworkConnectivityManager {

    override fun observeNetworkStatus(): Flow<NetworkStatus> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(NetworkStatus.CONNECTED)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                trySend(NetworkStatus.NOT_CONNECTED)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(NetworkStatus.NOT_CONNECTED)
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                trySend(NetworkStatus.LOSING)
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, callback)

        val currentState = isConnected(connectivityManager)
        trySend(if (currentState) NetworkStatus.CONNECTED else NetworkStatus.NOT_CONNECTED)

        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }

    private fun isConnected(
        connectivityManager: ConnectivityManager
    ): Boolean = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        ?: false
}

enum class NetworkStatus {
    CONNECTED,
    NOT_CONNECTED,
    LOSING
}
