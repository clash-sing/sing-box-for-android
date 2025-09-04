package com.clashsing.proxylib.schema

import android.os.Parcelable
import com.clashsing.proxylib.schema.singbox.Dns
import com.clashsing.proxylib.schema.singbox.Inbound
import com.clashsing.proxylib.schema.singbox.Outbound
import com.clashsing.proxylib.schema.singbox.Route
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class SingBox(
    val dns: Dns,
    val inbounds: List<Inbound>,
    val route: Route,
    val outbounds: List<Outbound>,
    val experimental: Exp = Exp(),
    val log: Log = Log()
) : Parcelable {
    @Parcelize
    @Serializable
    data class Log(
        val disabled: Boolean = false,
        val level: String = LOG_INFO,
        val timestamp: Boolean = true,
        val output: String? = null
    ) : Parcelable {
        companion object {
            const val LOG_TRACE = "trace"
            const val LOG_DEBUG = "debug"
            const val LOG_INFO = "info"
            const val LOG_WARN = "warn"
            const val LOG_ERROR = "error"
            const val LOG_FATAL = "fatal"
            const val LOG_PANIC = "panic"

        }
    }

    @Parcelize
    @Serializable
    data class Exp(
        @SerialName("cache_file")
        val cacheFile: CacheFile = CacheFile(),
    ) : Parcelable {
        @Parcelize
        @Serializable
        data class CacheFile(
            val enabled: Boolean = true,
            @SerialName("store_fakeip")
            val storeFakeip: Boolean = true
        ) : Parcelable

    }
}