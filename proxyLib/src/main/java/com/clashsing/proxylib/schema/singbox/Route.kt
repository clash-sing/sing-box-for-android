package com.clashsing.proxylib.schema.singbox

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Route(
    val rules: List<RouteRule>,
    @SerialName("rule_set")
    val ruleSet: List<RouteRuleSet>,

    /** 默认出站标签。如果为空，将使用第一个可用于对应协议的出站。 */
    val final: String? = null,

    /**
     * 默认将出站连接绑定到默认网卡，以防止在 tun 下出现路由环路。
     * 仅支持 Linux、Windows 和 macOS
     * 如果设置了 outbound.bind_interface 设置，则不生效。
     */
    @SerialName("auto_detect_interface")
    val autoDetectInterface: Boolean = true,

    /**
     * @see: https://sing-box.sagernet.org/configuration/shared/dial/#domain_resolver
     * 可以被 outbound.domain_resolver 覆盖。
     */
    @SerialName("default_domain_resolver")
    val defaultDomainResolver: String = "system"

)