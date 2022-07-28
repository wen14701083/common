package com.xhd.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException

/**
 * Content: 网络相关
 * Create by wk on 2021/9/15
 */
object NetUtils {

    @SuppressLint("MissingPermission")
    fun isWifiConnected(context: Context): Boolean {
        return getNetType(context) == NetType.NET_WIFI
    }

    @SuppressLint("MissingPermission")
    fun isConnected(context: Context): Boolean {
        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            ?: return false
        val networkInfo = connManager.activeNetworkInfo ?: return false
        return networkInfo.isConnected
    }

    @SuppressLint("MissingPermission")
    fun getNetType(context: Context): NetType {
        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            ?: return NetType.NET_NONE
        val networkInfo = connManager.activeNetworkInfo ?: return NetType.NET_NONE
        if (!networkInfo.isAvailable) {
            return NetType.NET_NONE
        }

        // 判断wifi
        val wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            ?: return NetType.NET_NONE
        var state = wifiInfo.state ?: return NetType.NET_NONE
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return NetType.NET_WIFI
        }

        // 判断移动网络
        val mobileInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            ?: return NetType.NET_NONE
        state = mobileInfo.state ?: return NetType.NET_NONE
        val strSubTypeName = networkInfo.subtypeName
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return when (networkInfo.subtype) {
                TelephonyManager.NETWORK_TYPE_GPRS,
                TelephonyManager.NETWORK_TYPE_CDMA,
                TelephonyManager.NETWORK_TYPE_EDGE,
                TelephonyManager.NETWORK_TYPE_1xRTT,
                TelephonyManager.NETWORK_TYPE_IDEN,
                TelephonyManager.NETWORK_TYPE_GSM
                -> NetType.NET_2G
                TelephonyManager.NETWORK_TYPE_EVDO_A,
                TelephonyManager.NETWORK_TYPE_UMTS,
                TelephonyManager.NETWORK_TYPE_EVDO_0,
                TelephonyManager.NETWORK_TYPE_HSDPA,
                TelephonyManager.NETWORK_TYPE_HSUPA,
                TelephonyManager.NETWORK_TYPE_HSPA,
                TelephonyManager.NETWORK_TYPE_EVDO_B,
                TelephonyManager.NETWORK_TYPE_EHRPD,
                TelephonyManager.NETWORK_TYPE_HSPAP,
                TelephonyManager.NETWORK_TYPE_IWLAN
                -> NetType.NET_3G
                TelephonyManager.NETWORK_TYPE_LTE -> NetType.NET_4G
                TelephonyManager.NETWORK_TYPE_NR -> NetType.NET_5G
                else ->
                    // 中国移动 联通 电信 三种3G制式
                    if ("TD-SCDMA".equals(strSubTypeName, ignoreCase = true) ||
                        "TD-WCDMA".equals(strSubTypeName, ignoreCase = true) ||
                        "CDMA2000".equals(strSubTypeName, ignoreCase = true)
                    ) {
                        NetType.NET_3G
                    } else {
                        NetType.NET_UNKNOWN
                    }
            }
        }
        return NetType.NET_NONE
    }

    fun getIPAddress(context: Context): String? {
        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            ?: return ""
        val info = connManager.activeNetworkInfo ?: return ""
        if (info.isConnected) {
            if (info.type == ConnectivityManager.TYPE_MOBILE) {
                try {
                    val en = NetworkInterface.getNetworkInterfaces()
                    while (en.hasMoreElements()) {
                        val intf = en.nextElement()
                        val enumIpAddr = intf.inetAddresses
                        while (enumIpAddr.hasMoreElements()) {
                            val inetAddress = enumIpAddr.nextElement()
                            if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                                return inetAddress.getHostAddress()
                            }
                        }
                    }
                } catch (e: SocketException) {
                    return ""
                }
            } else if (info.type == ConnectivityManager.TYPE_WIFI) {
                val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wifiInfo = wifiManager.connectionInfo
                // 调用方法将int转换为地址字符串
                return intIP2StringIP(wifiInfo.ipAddress)
            }
        } else {
            return ""
        }
        return ""
    }

    /**
     * 将得到的int类型的IP转换为String类型
     * @param ip
     * @return
     */
    fun intIP2StringIP(ip: Int): String {
        return (ip and 0xFF).toString() + "." + (ip shr 8 and 0xFF) + "." + (ip shr 16 and 0xFF) + "." + (ip shr 24 and 0xFF)
    }

    enum class NetType {
        NET_WIFI, NET_2G, NET_3G, NET_4G, NET_5G, NET_UNKNOWN, NET_NONE;

        fun getValueString(): String {
            return when (name) {
                NET_WIFI.name -> "wifi"
                NET_2G.name -> "2G"
                NET_3G.name -> "3G"
                NET_4G.name -> "4G"
                NET_5G.name -> "5G"
                NET_NONE.name -> "none"
                else -> "unknown"
            }
        }
    }
}
