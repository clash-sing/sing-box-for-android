package com.clashsing.proxylib.schema.singbox

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class SingBoxExp(
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