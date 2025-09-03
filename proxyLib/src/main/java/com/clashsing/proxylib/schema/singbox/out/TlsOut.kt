package com.clashsing.proxylib.schema.singbox.out

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class TlsOut(
    val enabled: Boolean = true,
    @SerialName("disable_sni")
    val disableSni: Boolean = false,
    @SerialName("server_name")
    val serverName: String = "",
    val insecure: Boolean = false,
    val alpn: List<String> = emptyList(),
    val utls: Utls? = null
) : Parcelable {
    @Parcelize
    @Serializable
    data class Utls(
        val enabled: Boolean = true,
        val fingerprint: String = CHROME,
    ) : Parcelable {
        companion object {
            const val CHROME = "chrome"
            const val FIREFOX = "firefox"
            const val EDGE = "edge"
            const val SAFARI = "safari"
            const val QQ = "qq"
            const val IOS = "ios"
            const val ANDROID = "android"
            const val RANDOM = "random"
            const val RANDOMIZED = "randomized"
        }
    }
}
