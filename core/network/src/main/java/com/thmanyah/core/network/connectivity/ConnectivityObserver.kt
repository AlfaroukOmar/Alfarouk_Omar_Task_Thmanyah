package com.thmanyah.core.network.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ConnectivityObserver @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val connectivity: Flow<ConnectivityStatus> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(ConnectivityStatus.Available)
            }

            override fun onLost(network: Network) {
                trySend(ConnectivityStatus.Lost)
            }
        }
        cm.registerDefaultNetworkCallback(callback)
        trySend(if (cm.isCurrentlyConnected()) ConnectivityStatus.Available else ConnectivityStatus.Lost)
        awaitClose { cm.unregisterNetworkCallback(callback) }
    }

    private fun ConnectivityManager.isCurrentlyConnected(): Boolean {
        val network = activeNetwork ?: return false
        val caps = getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
