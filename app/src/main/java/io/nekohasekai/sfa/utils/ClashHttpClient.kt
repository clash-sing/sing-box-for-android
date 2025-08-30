package io.nekohasekai.sfa.utils

import okhttp3.OkHttpClient
import okhttp3.Request
import okio.GzipSource
import okio.buffer
import java.io.Closeable
import java.util.concurrent.TimeUnit

class ClashHttpClient : Closeable {
    companion object {
        const val USER_AGENT = "clash.meta/v1.19.12"
        const val ACCEPT = "application/yaml, application/x-yaml, text/yaml, */*"
        const val ACCEPT_ENCODING = "gzip"
        /**
         * 用户的订阅信息
         * @sample: upload=4841978; download=345835144; total=214748364800; expire=1777514961
         */
        const val SUBSCRIPTION_USERINFO = "subscription-userinfo"
        /**
         * 代理服务提供商的网址
         * @sample: https://www.proxyXXX.com
         */
        const val PROFILE_WEB_PAGE_URL = "profile-web-page-url"

        /**
         * 通常用于保存代理服务提供商的名称
         * @sample: attachment;filename*=UTF-8''proxyXXX.com
         */
        const val CONTENT_DISPOSITION = "content-disposition"
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    fun getString(url: String): String {
        val result: Result<String> = runCatching {
            val request = Request.Builder().url(url)
                .header("User-Agent", USER_AGENT)
                .header("Accept", ACCEPT)
                .header("Accept-Encoding", ACCEPT_ENCODING)
                .build()
            val response = client.newCall(request).execute()
            val source = response.body.source()
            val gzipSource = if ("gzip" == response.header("Content-Encoding")) {
                GzipSource(source)
            } else {
                source
            }
            var content = ""
            gzipSource.buffer().use {
                val buffer = okio.Buffer()
                it.readAll(buffer)
                content = buffer.readString(response.body.contentType()?.charset() ?: Charsets.UTF_8)
            }
            content
        }
        return result.getOrThrow()
    }
    override fun close() {
        client.dispatcher.executorService.shutdown()
        client.connectionPool.evictAll()
        client.cache?.close()
    }
}