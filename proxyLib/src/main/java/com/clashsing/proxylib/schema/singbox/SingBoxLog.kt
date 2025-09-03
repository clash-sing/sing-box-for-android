package com.clashsing.proxylib.schema.singbox

import android.icu.util.Output
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class SingBoxLog(
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
