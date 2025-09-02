package io.nekohasekai.sfa.cs

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
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    fun getString(url: String): Result<ClashData> {
        return runCatching {
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
            ClashData.create(response.headers, content)
        }
    }
    override fun close() {
        client.dispatcher.executorService.shutdown()
        client.connectionPool.evictAll()
        client.cache?.close()
    }
}