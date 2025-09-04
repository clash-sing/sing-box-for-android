package com.clashsing.proxylib.schema.singbox.inbound
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class InTypeTun(
    override val type: String,
    override val tag: String,
    val address: List<String> = listOf("172.19.0.1/30", "fdfe:dcba:9876::1/126"),
    val mut: Int = 9000,
    /**
     * TCP/IP 栈
     * 默认使用 mixed 栈如果 gVisor 构建标记已启用，否则默认使用 system 栈
     */
    val stack: String = STACK_SYSTEM,
    @SerialName("auto_route")
    val autoRoute: Boolean = true,
    @SerialName("strict_route")
    val strictRoute: Boolean = true,
    /**
     * Deprecated, @see https://sing-box.sagernet.org/zh/migration/#_3
     */
    @Deprecated("入站选项已被弃用，且可以被规则动作替代。")
    val sniff: Boolean = true,

    /**
     * Deprecated, @see: https://sing-box.sagernet.org/zh/configuration/shared/listen/#sniff_override_destination
     * 在 Inbound.Tun (https://sing-box.sagernet.org/zh/configuration/inbound/tun/) 中无说明
     */
    @Deprecated("仅在【监听字段】中有说明，且已经在 v1.11.0 版本中废弃。")
    @SerialName("sniff_override_destination")
    val sniffOverrideDestination: Boolean = true,
    val platform: Platform = Platform()

) : InType {
    companion object {
        const val STACK_SYSTEM = "system"
        const val STACK_GVISOR = "gvisor"
        const val STACK_MIXED = "mixed"
    }
    @Parcelize
    @Serializable
    data class Platform(
        @SerialName("http_proxy")
        val httpProxy: HttpProxy = HttpProxy(),
    ) : Parcelable {
        @Parcelize
        @Serializable
        data class HttpProxy(
            val enabled: Boolean = false,
            val server: String = "127.0.0.1",
            @SerialName("server_port")
            val serverPort: Int = 8890
        ) : Parcelable
    }
}
