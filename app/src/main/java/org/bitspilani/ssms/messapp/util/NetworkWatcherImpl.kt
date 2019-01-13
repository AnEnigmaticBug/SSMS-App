package org.bitspilani.ssms.messapp.util

import android.content.Context
import android.net.ConnectivityManager

class NetworkWatcherImpl(private val context: Context) : NetworkWatcher {

    override fun isConnectedToInternet(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }
}