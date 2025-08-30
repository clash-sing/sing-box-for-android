package io.nekohasekai.sfa.clash

/** Types of Outbound
 * @see: https://wiki.metacubex.one/config/proxies
 */
object ClashMetaOutboundType {
    const val DIRECT = "direct"
    const val DNS = "dns"
    const val HTTP = "http"
    const val SOCKS = "socks"
    const val SOCKS5 = "socks5"
    const val SHADOWSOCKS = "ss"
    const val SHADOWSOCKSR = "ssr"
    const val MIERU = "mieru"
    const val SNELL = "snell"
    const val VMESS = "vmess"
    const val VLESS = "vless"
    const val TROJAN = "trojan"
    const val ANYTLS = "anytls"
    const val HYSTERIA = "hysteria"
    const val HYSTERIA2 = "hysteria2"
    const val TUIC = "tuic"
    const val WIREGUARD = "wireguard"
    const val SSH = "ssh"
    /** 手动选择 组 */
    const val SELECT = "select"
    /** 自动选择 组 */
    const val URL_TEST = "url-test"
    /** 自动回退 组
     * 当前节点超时时，则会按代理顺序选择第一个可用节点
     */
    const val FALLBACK = "fallback"
    /** 负载均衡 组 */
    const val LOAD_BALANCE = "load-balance"
    /**
     * 链式代理 组
     * 流量去向为 Clash <-> http <-> vmess <-> ss1 <-> ss2 <-> Internet
     * relay 支持传输 UDP，前提是代理链的头尾节点都要支持 UDP over TCP。
     * 目前支持 udp 的协议有: vmess/vless/trojan/ss/ssr/tuic
     * Warning:
     * relay 策略即将被弃用，请使用 dialer-proxy。
     * wireguard 目前不支持在 relay 中使用，也请使用 dialer-proxy。
     * dialer-proxy @see: https://wiki.metacubex.one/config/proxies/dialer-proxy
     */
    const val RELAY = "relay"
}