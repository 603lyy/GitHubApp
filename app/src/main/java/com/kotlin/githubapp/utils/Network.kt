package com.kotlin.githubapp.utils

import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import com.kotlin.githubapp.AppContext
import org.jetbrains.anko.connectivityManager


object Network {
    //    fun isAvailable(): Boolean = AppContext.connectivityManager.activeNetworkInfo?.isAvailable ?: false
    fun isAvailable(): Boolean {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val networkCapabilities: NetworkCapabilities? =
                AppContext.connectivityManager.getNetworkCapabilities(AppContext.connectivityManager.activeNetwork)

            if (networkCapabilities != null) {
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            } else {
                false
            }
        } else {
            val networkInfo = AppContext.connectivityManager?.activeNetworkInfo as NetworkInfo
            networkInfo.isConnected && networkInfo.isAvailable
        }
    }
}