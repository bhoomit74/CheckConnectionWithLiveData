package com.bhoomit.connection.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CheckConnection(context : Context) : LiveData<Boolean>() {

    private lateinit var mNetworkCallback: ConnectivityManager.NetworkCallback
    private var connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var validNetworks : MutableSet<Network> = HashSet()  // to store all the network

    private fun checkValidNetworks(){
        postValue(validNetworks.size > 0)
    }

    override fun onActive() {
        super.onActive()
        mNetworkCallback = networkCallback()
        val networkRequest = NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
        connectivityManager.registerNetworkCallback(networkRequest,mNetworkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(mNetworkCallback)
    }

    private fun networkCallback() = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            val hasInternetCapability = networkCapabilities?.hasCapability(NET_CAPABILITY_INTERNET)
            if (hasInternetCapability == true) {
                // check if this network actually has internet
                CoroutineScope(Dispatchers.IO).launch {
                    val hasInternet = CheckInternetBySocketConnection.isInternetAvailable(network.socketFactory)
                    if(hasInternet){
                        withContext(Dispatchers.Main){
                            validNetworks.add(network)
                            checkValidNetworks()
                        }
                    }
                }
            }

        }

        override fun onLost(network: Network) {
            super.onLost(network)
            validNetworks.remove(network)
            checkValidNetworks()
        }

    }



}