package io.nekohasekai.sfa.clash

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import kotlinx.parcelize.Parcelize

object SubscriptionUserinfoManager {
    private val mmkv: MMKV = MMKV.mmkvWithID("subscription_userinfo", MMKV.MULTI_PROCESS_MODE)
    fun setUserinfo(profileId: Long, userinfo: SubscriptionUserinfo) {
        mmkv.encode("profile_$profileId", userinfo)
    }
    fun getUserinfo(profileId: Long): SubscriptionUserinfo? {
        return mmkv.decodeParcelable("profile_$profileId", SubscriptionUserinfo::class.java)
    }

    fun remove(profileId: Long) {
        mmkv.removeValueForKey("profile_$profileId")
    }
    fun clear() {
        mmkv.clearAll()
    }
}

@Parcelize
data class SubscriptionUserinfo(
    /** 当月使用量，单位：Byte */
    val usedBytes: Long?,
    /** 每月限额，单位：Byte */
    val totalBytes: Long?,
    /** 订阅到期日 */
    val expireTimestamp: Long?,
    /** 代理服务提供商的网址 */
    val spUrl: String = "",
    /** 代理服务提供商的名称 */
    val spDisposition: String = ""
) : Parcelable