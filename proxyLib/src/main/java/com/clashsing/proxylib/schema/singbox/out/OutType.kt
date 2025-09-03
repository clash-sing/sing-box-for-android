package com.clashsing.proxylib.schema.singbox.out

import android.os.Parcelable


interface OutType : Parcelable {
    val type: String
    val tag: String
    companion object {
        const val DIRECT = "direct"
        const val BLOCK = "block"
        const val SOCKS = "socks"
        const val HTTP = "http"
        const val SHADOWSOCKS = "shadowsocks"
        const val VMESS = "vmess"
        const val TROJAN = "trojan"
        const val WIREGUARD = "wireguard"
        const val HYSTERIA = "hysteria"
        const val VLESS = "vless"
        const val SHADOWTLS = "shadowtls"
        const val TUIC = "tuic"
        const val HYSTERIA2 = "hysteria2"
        const val ANYTLS = "anytls"
        const val TOR = "tor"
        const val SSH = "ssh"
        const val DNS = "dns"
        const val SELECTOR = "selector"
        const val URLTEST = "urltest"
    }
}