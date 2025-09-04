package com.clashsing.proxylib.schema.singbox.inbound

import android.os.Parcelable


interface InType : Parcelable {
    val type: String
    val tag: String
    companion object {
        const val DIRECT = "direct"
        const val MIXED = "mixed"
        const val SOCKS = "socks"
        const val HTTP = "http"
        const val SHADOWSOCKS = "shadowsocks"
        const val VMESS = "vmess"
        const val TROJAN = "trojan"
        const val NATIVE = "naive"
        const val HYSTERIA = "hysteria"
        const val SHADOWTLS = "shadowtls"
        const val TUIC = "tuic"
        const val HYSTERIA2 = "hysteria2"
        const val VLESS = "vless"
        const val ANYTLS = "anytls"
        const val TUN = "tun"
        const val REDIRECT = "redirect"
        const val TPROXY = "tproxy"
    }
}