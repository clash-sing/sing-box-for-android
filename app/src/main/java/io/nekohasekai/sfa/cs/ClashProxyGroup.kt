package io.nekohasekai.sfa.cs

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClashProxyGroup(
    val name: String,
    val type: String,
    val proxies: MutableList<String>,
    val url: String? = null,
    val interval: Int? = null,
) : Parcelable {
    companion object {
        fun create(group: Map<String, Any?>): ClashProxyGroup? {
            val name = group["name"] as? String
            val type = group["type"] as? String
            @Suppress("UNCHECKED_CAST")
            val proxies = group["proxies"] as? MutableList<String> ?: mutableListOf()
            if (name == null || type == null || proxies.isEmpty()) {
                return null
            } else {
                val url = group["url"] as? String
                val interval = if (group["interval"] is Int) group["interval"] as Int else null
                return ClashProxyGroup(name, type, proxies, url, interval)
            }
        }
    }
}
