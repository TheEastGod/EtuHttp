package com.zxd.httpdemo

import android.util.Log
import com.koushikdutta.async.AsyncServer
import com.koushikdutta.async.http.WebSocket
import com.koushikdutta.async.http.server.AsyncHttpServer
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*
import java.util.regex.Pattern


/**
 * author: zxd
 * created on: 2021/5/26 17:58
 * description:
 */
class MockWebServerHandler private constructor(){


    var callBack: MockWebServerCallBack? = null
    var mWebSocket : WebSocket? = null

    companion object{
        @JvmStatic
        val instances : MockWebServerHandler by lazy (mode = LazyThreadSafetyMode.SYNCHRONIZED){
                MockWebServerHandler()
        }
    }

    interface MockWebServerCallBack{
        fun onConnect(hostName:String,port:Int)

        fun onMessage(text: String)

        fun onFailure(t: Throwable)
    }

    fun setMockWebServerCallBack(mockWebServerCallBack: MockWebServerCallBack){
        callBack = mockWebServerCallBack
    }

    fun sendMessage(msg:String){
        Log.i("MockWebServerHandler","msg   $msg")
        mWebSocket?.send(msg)
    }

    fun connect(){

        val httpServer = AsyncHttpServer()
        httpServer.listen(AsyncServer.getDefault(), 5051)
        httpServer.websocket("/live") { webSocket, request ->

            mWebSocket = webSocket

            webSocket.setStringCallback {
                handlerMsg(it)
                callBack?.onMessage(it)
            }

            webSocket.setPingCallback {
                webSocket.pong("pong")
            }

            webSocket.setClosedCallback {
                callBack?.onFailure(it)
            }

        }

        callBack?.onConnect(getLocalIPAddress()?.hostAddress ?: "",5051)
//        val runnable = Runnable {
//
//        }
//
//
//        ThreadPoolManager.instance.startThread(runnable)

    }

    private fun handlerMsg(it: String) {

        val param  :String = "1023232&SOCKET\r\n"  +"{\n" +
                "    \"code\": 2,\n" +
                "    \"message\": 身份验证不通过\n " +
                "}"

        sendMessage(param)
    }


    fun getLocalIPAddress(): InetAddress? {
        var enumeration: Enumeration<NetworkInterface>? = null
        try {
            enumeration = NetworkInterface.getNetworkInterfaces()
        } catch (e: SocketException) {
            e.printStackTrace()
        }
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                val nif = enumeration.nextElement()
                val inetAddresses =
                    nif.inetAddresses
                if (inetAddresses != null) {
                    while (inetAddresses.hasMoreElements()) {
                        val inetAddress = inetAddresses.nextElement()
                        if (!inetAddress.isLoopbackAddress && isIPv4Address(inetAddress.hostAddress)) {
                            return inetAddress
                        }
                    }
                }
            }
        }
        return null
    }


    /**
     * Ipv4 address check.
     */
    private val IPV4_PATTERN = Pattern.compile(
        "^(" + "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
                "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$"
    )

    /**
     * Check if valid IPV4 address.
     *
     * @param input the address string to check for validity.
     * @return True if the input parameter is a valid IPv4 address.
     */
    fun isIPv4Address(input: String?): Boolean {
        return IPV4_PATTERN.matcher(input).matches()
    }


}