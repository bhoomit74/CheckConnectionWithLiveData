package com.bhoomit.connection.util

import android.util.Log
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory

object CheckInternetBySocketConnection {

    /**
     * Send a ping to google's primary DNS.
     * If successful, that means we have internet.
     */

    fun isInternetAvailable(socketFactory: SocketFactory) : Boolean{
        return try {
            val socket = socketFactory.createSocket() ?: throw IOException("Socket is null")
            socket.connect(InetSocketAddress("8.8.8.8",53),1500)
            socket.close()
            Log.d("INTERNET_LOG","Internet Available")
            true
        }catch (exception : IOException){
            Log.d("INTERNET_LOG", exception.toString())
            false
        }
    }

}