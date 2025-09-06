package com.clashsing.proxylib

import com.clashsing.proxylib.schema.customJson
import com.tencent.mmkv.MMKV

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