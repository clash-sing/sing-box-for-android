package com.clashsing.proxylib

import kotlinx.serialization.Serializable

@Serializable
data class SubUserinfo(
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
)
