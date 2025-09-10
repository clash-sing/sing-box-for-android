package com.clashsing.proxylib.parser

import android.net.Uri
import com.clashsing.proxylib.SubUserinfo
import com.clashsing.proxylib.schema.SingBox
import com.clashsing.proxylib.schema.clash.Clash
import com.clashsing.proxylib.schema.clash.Proxy
import com.clashsing.proxylib.schema.clash.ProxyGroup
import com.clashsing.proxylib.schema.customJson
import com.clashsing.proxylib.schema.decodeFromMap
import com.clashsing.proxylib.schema.singbox.Outbound
import com.clashsing.proxylib.schema.singbox.RouteRule
import okhttp3.Headers
import org.yaml.snakeyaml.Yaml
import kotlin.text.split

class SubParserClash(originSingBox: SingBox?, srcContent: String, headers: Headers) : SubParser(originSingBox, srcContent, headers) {

    private val willRemovedGroup = mutableListOf<ProxyGroup>()
    companion object {
        /**
         * 用户的订阅信息
         * @sample: upload=4841978; download=345835144; total=214748364800; expire=1777514961
         */
        const val SUBSCRIPTION_USERINFO = "subscription-userinfo"
        /**
         * 代理服务提供商的网址
         * @sample: https://www.proxyXXX.com
         */
        const val PROFILE_WEB_PAGE_URL = "profile-web-page-url"

        /**
         * 通常用于保存代理服务提供商的名称
         * @sample: attachment;filename*=UTF-8''proxyXXX.com
         */
        const val CONTENT_DISPOSITION = "content-disposition"

        fun convert2SingBoxType(type: String): String {
            return when (type) {
                Proxy.Type.HYSTERIA2 -> Outbound.Type.HYSTERIA2
                Proxy.Type.HYSTERIA -> Outbound.Type.HYSTERIA
                Proxy.Type.TROJAN -> Outbound.Type.TROJAN
                Proxy.Type.ANYTLS -> Outbound.Type.ANYTLS
                Proxy.Type.SHADOWSOCKS -> Outbound.Type.SHADOWSOCKS
                Proxy.Type.VLESS -> Outbound.Type.VLESS
                ProxyGroup.Type.SELECT -> Outbound.Type.SELECTOR
                ProxyGroup.Type.URL_TEST -> Outbound.Type.URLTEST
                else -> throw IllegalArgumentException("unsupported clash proxy type: $type")
            }
        }
    }

    override suspend fun getSingBox(): SingBox? {
/*
        TODO("未实现 VLESS 协议")
        val vlessOutbound = originSingBox?.outbounds?.find { it.type == Outbound.Type.VLESS }
        if (vlessOutbound != null) {
            throw IllegalArgumentException("直接由 sing-box 处理。")
        }
*/

        this._singBox = originSingBox ?: getDefaultSingBox()
        val map = Yaml().load<Map<String, Any?>>(srcContent)
        val clash = customJson.decodeFromMap<Clash>(map)
        if (singBox?.outbounds.isNullOrEmpty()) {
            this.singBox?.outbounds?.add(Outbound.direct())
            clash.proxies.forEach {
                val outbound = convert2Outbound(it)
                this.singBox?.outbounds?.add(outbound)
            }
        } else {
            singBox?.route?.final = null
            singBox?.outbounds?.removeIf { !it.outbounds.isNullOrEmpty() }
        }

        // 添加 block 出站，添加 clash_mode = "block"
        val blockRouteRule = this.singBox?.route?.rules?.firstOrNull {
            it.action == RouteRule.Action.REJECT
        }
        if (blockRouteRule != null) {
            val blockOutbound = Outbound.block()
            this.singBox?.outbounds?.add(0, blockOutbound)
            blockRouteRule.clashMode = RouteRule.ClashMode.BLOCK
            blockRouteRule.outbound = blockOutbound.tag
            blockRouteRule.protocols = null

//            this.singBox?.route?.rules?.add(RouteRule(action = RouteRule.Action.ROUTE, clashMode = RouteRule.ClashMode.BLOCK, outbound = blockOutbound.tag))
        }

        clash.proxyGroups.reversed().forEach { proxyGroup ->
            val outbound = convert2Outbound(proxyGroup)
            if (outbound != null) {
                this.singBox?.outbounds?.add(0, outbound)
            }
        }
        // sing-box 不支持 [Outbound.type] = [ProxyGroup.Type.FALLBACK] 的节点（代理组），需要移除。
        if (this.singBox?.outbounds?.isNotEmpty() ?: false) {
            willRemovedGroup.forEach { proxyGroup ->
                this.singBox?.outbounds?.forEach { outbound ->
                    if (outbound.outbounds?.isNotEmpty() ?: false) {
                        if (outbound.outbounds.contains(proxyGroup.name)) {
                            outbound.outbounds.remove(proxyGroup.name)
                        }
                    }
                }
            }
        }

        // 修正 GLOBAL 模式
        val globalRouteRule = this.singBox?.route?.rules?.firstOrNull {
            it.clashMode == RouteRule.ClashMode.GLOBAL
        }
        if (globalRouteRule != null) {
            val outbound = this.singBox?.outbounds?.firstOrNull {
                !it.outbounds.isNullOrEmpty() && it.type == Outbound.Type.SELECTOR
            }
            if (outbound != null) {
                globalRouteRule.outbound = outbound.tag
            }
        }

        return this.singBox
    }

    private fun convert2Outbound(proxy: Proxy): Outbound {
        val type = convert2SingBoxType(proxy.type)
        return when (type) {
            Outbound.Type.HYSTERIA2 -> Outbound.hysteria2(
                tag = proxy.name,
                server = proxy.server,
                serverPort = proxy.port,
                serverPorts = listOf((proxy.ports ?: "").replace("-", ":")),
                upMbps = null,
                downMbps = null,
                password = proxy.password!!,
                tls = Outbound.Tls(
                    enabled = true,
                    disableSni = true,
                    insecure = true,
                    serverName = "",
                    alpn = proxy.alpn ?: listOf("h3")
                )
            )
            Outbound.Type.HYSTERIA -> Outbound.hysteria(
                tag = proxy.name,
                server = proxy.server,
                serverPort = proxy.port,
                serverPorts = listOf((proxy.ports ?: "").replace("-", ":")),
                upMbps = proxy.up ?: 100,
                downMbps = proxy.down ?: 100,
                authStr = proxy.authStr!!,
                disableMtuDiscovery = proxy.disableMtuDiscovery ?: true,
                tls = Outbound.Tls(
                    enabled = true,
                    disableSni = true,
                    insecure = true,
                    serverName = "",
                    alpn = proxy.alpn ?: listOf("h3")
                )
            )
            Outbound.Type.TROJAN -> Outbound.trojan(
                tag = proxy.name,
                server = proxy.server,
                serverPort = proxy.port,
                password = proxy.password!!,
                tls = Outbound.Tls(
                    enabled = true,
                    disableSni = true,
                    insecure = true,
                    serverName = ""
                ),
                transport = Outbound.Transport(
                    type = proxy.network ?: Outbound.Transport.WEB_SOCKET
                )
            )
            Outbound.Type.ANYTLS -> Outbound.anyTls(
                tag = proxy.name,
                server = proxy.server,
                serverPort = proxy.port,
                password = proxy.password!!,
                tls = Outbound.Tls(
                    enabled = true,
                    disableSni = proxy.sni.isNullOrBlank(),
                    insecure = true,
                    serverName = proxy.sni ?: "",
                    utls = if (proxy.clientFingerprint.isNullOrBlank()) null else Outbound.Tls.Utls(enabled = true, fingerprint = proxy.clientFingerprint)
                )
            )
            Outbound.Type.VLESS -> Outbound.vless(
                tag = proxy.name,
                server = proxy.server,
                serverPort = proxy.port,
                uuid = proxy.uuid!!,
                flow = proxy.flow,
                network = if (proxy.udp ?: false) Outbound.Network.UDP else Outbound.Network.TCP,
                tls = Outbound.Tls(
                    enabled = true,
                    disableSni = proxy.serverName.isNullOrBlank(),
                    insecure = true,
                    serverName = proxy.serverName ?: "",
                    utls = if (proxy.clientFingerprint.isNullOrBlank()) null else Outbound.Tls.Utls(enabled = true, fingerprint = proxy.clientFingerprint),
                    reality = if (proxy.realityOpts?.publicKey.isNullOrBlank()) null else Outbound.Tls.Reality(
                        enabled = true,
                        shortId = proxy.realityOpts.shortId ?: throw Exception("reality shortId is null"),
                        publicKey = proxy.realityOpts.publicKey
                    )
                )
            )
            Outbound.Type.SHADOWSOCKS -> Outbound.shadowsocks(
                tag = proxy.name,
                server = proxy.server,
                serverPort = proxy.port,
                password = proxy.password!!,
                method = proxy.cipher ?: "",
                network = if (proxy.udp ?: false) Outbound.Network.UDP else Outbound.Network.TCP
            )
            else -> throw IllegalArgumentException("unsupported clash proxy type: ${proxy.type}")
        }
    }

    private fun convert2Outbound(group: ProxyGroup): Outbound? {
        if (group.type == ProxyGroup.Type.FALLBACK) {
            willRemovedGroup.add(group)
            return null
        }
        val type = convert2SingBoxType(group.type)
        return when (type) {
            Outbound.Type.SELECTOR -> Outbound.selector(
                tag = group.name,
                outbounds = group.proxies.toMutableList()
            )
            Outbound.Type.URLTEST -> Outbound.urltest(
                tag = group.name,
                outbounds = group.proxies.toMutableList(),
                url = group.url ?: "https://www.gstatic.com/generate_204",
                interval = if (group.interval == null) "3m" else "${ (if (group.interval > 600) 300 else group.interval) / 60}m",
                tolerance = 50
            )
            else -> throw IllegalArgumentException("unsupported clash proxy type: ${group.type}")
        }
    }

    override fun getSubUserInfo(): SubUserinfo? {
        var usedBytes: Long? = null
        var totalBytes: Long? = null
        var expireTimestamp: Long? = null
        headers[SUBSCRIPTION_USERINFO]?.let { subscription ->
            val subscriptionArray = subscription.split(";")
            if (subscriptionArray.size == 4) {
                val uploadBytes: Long? = getSubscriptionValue("upload=", subscriptionArray[0])
                val downloadBytes: Long? = getSubscriptionValue("download=", subscriptionArray[1])
                if (uploadBytes != null || downloadBytes != null) usedBytes = (uploadBytes ?: 0) + (downloadBytes ?: 0)
                totalBytes = getSubscriptionValue("total=", subscriptionArray[2])
                expireTimestamp = getSubscriptionValue("expire=", subscriptionArray[3])
                if (expireTimestamp != null && expireTimestamp.toString().length == 10) {
                    expireTimestamp *= 1000
                }
            }
        }
        val profileWebPageUrl: String? = headers[PROFILE_WEB_PAGE_URL]
        val contentDisposition: String? = headers[CONTENT_DISPOSITION]?.let { disposition ->
            disposition.split("''", ignoreCase = true).takeIf { it.size == 2 }?.get(1)
        }
        if (usedBytes == null && totalBytes == null && expireTimestamp == null
            && profileWebPageUrl.isNullOrBlank() && contentDisposition.isNullOrBlank()) return null
        return SubUserinfo(
            usedBytes = usedBytes,
            totalBytes = totalBytes,
            expireTimestamp = expireTimestamp,
            spUrl = profileWebPageUrl,
            spDisposition = if (contentDisposition.isNullOrBlank()) null else Uri.decode(contentDisposition)
        )
    }

    private fun getSubscriptionValue(key: String, text: String): Long? {
        return text.let { value ->
            value.takeIf {
                it.contains(key, true)
            }?.replace(key, "", true)?.trim()?.toLongOrNull()
        }
    }

}