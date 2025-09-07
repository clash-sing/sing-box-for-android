package com.clashsing.proxylib.parser

import android.net.Uri
import android.util.Log
import com.clashsing.proxylib.SubUserinfo
import com.clashsing.proxylib.schema.SingBox
import okhttp3.Headers
import kotlin.io.encoding.Base64
import androidx.core.net.toUri
import com.clashsing.proxylib.ProxyComponent
import com.clashsing.proxylib.R
import com.clashsing.proxylib.schema.singbox.Outbound
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class SubParserRocket(originSingBox: SingBox?, srcContent: String, headers: Headers) : SubParser(originSingBox, srcContent, headers) {
    private var decodeContent: String? = null
    private var statusLine: String? = null
    private val outbounds: MutableList<Outbound> = mutableListOf()

    override suspend fun getSingBox(): SingBox? {
        val byteArray = Base64.decode(srcContent)
        decodeContent = String(byteArray, Charsets.UTF_8)
        decodeContent?.lineSequence()?.forEach { line ->
            if (line.uppercase().startsWith("STATUS")) {
                statusLine = line
            } else {
                val uri = line.toUri()
                val outbound = parseUri(uri)
                if (outbound != null) {
                    outbounds.add(outbound)
                }
            }
            Log.d("Rocket", line)
        }
        if (outbounds.isNotEmpty()) {
            this._singBox = getDefaultSingBox()
            this.singBox?.let {
                it.outbounds.add(Outbound.direct())
                it.outbounds.addAll(outbounds)

                val urlTestTag = ProxyComponent.application.getString(R.string.proxy_lib_url_test_default_tag)
                val urlTestOutbound = Outbound.urltest(tag = urlTestTag, outbounds = outbounds.map { outbound -> outbound.tag }.toMutableList())
                it.outbounds.add(0, urlTestOutbound)

                val selectorOutbound = Outbound.selector(outbounds = outbounds.map { outbound -> outbound.tag }.toMutableList())
                selectorOutbound.outbounds?.add(0, urlTestTag)
                it.outbounds.add(0, selectorOutbound)
            }
        }
        return this.singBox
    }

    private fun parseUri(uri: Uri): Outbound? {
        return when (uri.scheme) {
            Schema.HYSTERIA2 -> {
                Outbound.hysteria2(
                    tag = uri.fragment ?: return null,
                    server = uri.host ?: return null,
                    serverPort = uri.port.toLong(),
                    password = uri.userInfo ?: return null,
                    serverPorts = listOf(uri.getQueryParameter("mport")?.replace("-", ":") ?: return null),
                    tls = Outbound.Tls(
                        enabled = true,
                        disableSni = true,
                        serverName = "",
                        insecure = true
                    )
                )
            }
            Schema.HYSTERIA -> {
                Outbound.hysteria(
                    tag = uri.fragment ?: return null,
                    server = uri.host ?: return null,
                    serverPort = uri.port.toLong(),
                    authStr = uri.getQueryParameter("auth") ?: return null,
                    serverPorts = listOf(uri.getQueryParameter("mport")?.replace("-", ":") ?: return null),
                    upMbps = uri.getQueryParameter("upmbps")?.toLong() ?: 100,
                    downMbps = uri.getQueryParameter("downmbps")?.toLong() ?: 100,
                    disableMtuDiscovery = true,
                    tls = Outbound.Tls(
                        enabled = true,
                        disableSni = true,
                        serverName = "",
                        insecure = true,
                        alpn = if (uri.getQueryParameter("alpn").isNullOrBlank()) null
                        else listOf(uri.getQueryParameter("alpn")!!),
                    )
                )
            }
            Schema.TROJAN -> {
                Outbound.trojan(
                    tag = uri.fragment ?: return null,
                    server = uri.host ?: return null,
                    serverPort = uri.port.toLong(),
                    password = uri.userInfo ?: return null,
                    tls = Outbound.Tls(
                        enabled = true,
                        disableSni = true,
                        serverName = "",
                        insecure = true
                    ),
                    transport = Outbound.Transport(type = Outbound.Transport.WEB_SOCKET)
                )
            }
            Schema.ANYTLS -> {
                Outbound.anyTls(
                    tag = uri.fragment ?: return null,
                    server = uri.host ?: return null,
                    serverPort = uri.port.toLong(),
                    password = uri.userInfo ?: return null,
                    tls = Outbound.Tls(
                        enabled = true,
                        disableSni = uri.getQueryParameter("peer").isNullOrBlank(),
                        serverName = uri.getQueryParameter("peer") ?: "",
                        insecure = true
                    )
                )
            }
            else -> {
                Log.w("ShadowRocket", "Unsupported schema: ${uri.scheme}")
                return null
            }
        }
    }

    /**
     * @sample:
     *      原值：STATUS=🚀↑:0.58GB,↓:4.31GB,TOT:200GB💡Expires:2026-04-30
     *      Encode：STATUS%3D%F0%9F%9A%80%E2%86%91%3A0.58GB%2C%E2%86%93%3A4.31GB%2CTOT%3A200GB%F0%9F%92%A1Expires%3A2026-04-30
     */
    override fun getSubUserInfo(): SubUserinfo? {
        if (statusLine == null) {
            statusLine = decodeContent?.lineSequence()?.firstOrNull { line ->
                line.uppercase().startsWith("STATUS")
            }
        }
        var subUserinfo: SubUserinfo? = null
        if (!statusLine.isNullOrBlank()) {
            val encodeStatus = Uri.encode(statusLine!!)
            val expiresTag = "Expires%3A"
            var used: Long? = null
            var total: Long? = null
            var expireTimestamp: Long? = null
            val usedAndTotal = encodeStatus.substring(0, encodeStatus.indexOf(expiresTag)-12)
            if (usedAndTotal.isNotEmpty()) {
                val usedAndTotalArray =usedAndTotal.split("%2C")
                if (usedAndTotalArray.size == 3) {
                    val upload = usedString2Long(usedAndTotalArray[0])
                    val download = usedString2Long(usedAndTotalArray[1])
                    if (upload != null || download != null) {
                        used = (upload ?: 0) + (download ?: 0)
                    }
                    total = usedString2Long(usedAndTotalArray[2])
                }
            }
            if (encodeStatus.contains(expiresTag)) {
                val strExpires = encodeStatus!!.split(expiresTag)[1]
                val formatter = DateTimeFormatter.ISO_LOCAL_DATE // yyyy-MM-dd
                try {
                    val localDate = LocalDate.parse(strExpires, formatter)
                    val zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault())
                    expireTimestamp = zonedDateTime.toInstant().toEpochMilli()
                } catch (e: Exception) {
                    Log.e("SubParserRocket", "parse expires error: $e")
                }
            }
            if (used != null || total != null || expireTimestamp != null) {
                subUserinfo = SubUserinfo(
                    usedBytes = used,
                    totalBytes = total,
                    expireTimestamp = expireTimestamp,
                    spUrl = null,
                    spDisposition = null
                )
            }
        }
        return subUserinfo
    }

    private fun usedString2Long(str: String): Long? {
        if (str.contains("%3A")) {
            val strValue = str.substring(str.indexOf("%3A") + 3)
            if (strValue.uppercase().contains("GB")) {
                return (strValue.substring(0, strValue.indexOf("GB")).toFloat() * 1024 * 1024 * 1024).toLong()
            } else if (strValue.uppercase().contains("MB")) {
                return (strValue.substring(0, strValue.indexOf("MB")).toFloat() * 1024 * 1024).toLong()
            } else if (strValue.uppercase().contains("KB")) {
                return (strValue.substring(0, strValue.indexOf("KB")).toFloat() * 1024).toLong()
            }
        }
        return null
    }

    object Schema {
        const val TROJAN = "trojan"
        const val HYSTERIA = "hysteria"
        const val HYSTERIA2 = "hysteria2"
        const val ANYTLS = "anytls"
    }
}