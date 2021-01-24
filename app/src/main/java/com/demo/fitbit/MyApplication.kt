package com.demo.fitbit

import android.app.Application
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * FitBit Demo Application class
 */
class MyApplication : Application() {
    val TAG = javaClass.simpleName

    companion object {
        @get:Synchronized
        var instance: MyApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo: NetworkInfo?
            activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
}