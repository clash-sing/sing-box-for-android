package com.clashsing.proxylib

import com.clashsing.proxylib.schema.customJson
import com.tencent.mmkv.MMKV
import kotlinx.serialization.Serializable

object SubUserinfoManager {

    private val mmkv: MMKV = MMKV.mmkvWithID("subscription_userinfo", MMKV.MULTI_PROCESS_MODE)
    fun setUserinfo(profileId: Long, userinfo: SubUserinfo) {
        mmkv.encode("profile_$profileId", customJson.encodeToString(userinfo))
    }
    fun getUserinfo(profileId: Long): SubUserinfo? {
        val json = mmkv.decodeString("profile_$profileId", null)
        return if (json == null) null else customJson.decodeFromString<SubUserinfo>(json)
    }

    fun remove(profileId: Long) {
        mmkv.removeValueForKey("profile_$profileId")
    }
    fun clear() {
        mmkv.clearAll()
    }

}

@Serializable
data class SubUserinfo(
    /** 当月使用量，单位：Byte */
    val usedBytes: Long?,
    /** 每月限额，单位：Byte */
    val totalBytes: Long?,
    /** 订阅到期日 */
    val expireTimestamp: Long?,
    /** 代理服务提供商的网址 */
    val spUrl: String?,
    /** 代理服务提供商的名称 */
    val spDisposition: String?
)