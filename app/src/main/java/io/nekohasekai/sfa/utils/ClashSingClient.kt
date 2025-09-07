package io.nekohasekai.sfa.utils

import android.util.Log
import android.webkit.WebSettings
import androidx.annotation.WorkerThread
import com.clashsing.proxylib.SubUserinfoManager
import com.clashsing.proxylib.parser.SubParser
import com.clashsing.proxylib.parser.SubParserClash
import com.clashsing.proxylib.parser.SubParserRocket
import com.clashsing.proxylib.schema.customJson
import io.nekohasekai.sfa.Application
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Buffer
import okio.BufferedSource
import okio.GzipSource
import okio.buffer
import java.io.Closeable
import java.util.concurrent.TimeUnit

class ClashSingClient(val profileId: Long) : Closeable {
    companion object {
        const val USER_AGENT = "SFA/1.12.4 mihomo/1.19.12 ClashMeta clash-verge v2ray"
        const val ACCEPT = "application/json, application/yaml;q=0.8, text/plain;q=0.5"
        const val ACCEPT_ENCODING = "gzip"
    }

    private val client = OkHttpClient.Builder()
//        .connectTimeout(10, TimeUnit.SECONDS)
//        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    @WorkerThread
    suspend fun getString(url: String): String {
        val resultClashSing = runCatching {
            var subParser: SubParser? = null
            val wrapper = getClashSingString(url)
            val srcContent = wrapper.content
            val firstLine = srcContent.lineSequence().first()
            val headers = wrapper.headers
            if (firstLine.startsWith("{")) { // Json
                return srcContent
            } else if (firstLine.contains(":")) { // Yaml
                try {
                    subParser = SubParserClash(srcContent, headers)
                } catch (e: Exception) {
                    Log.e("ClashSingClient", "Yaml decode failed.", e)
                }
            } else { // Base64
                try {
                    subParser = SubParserRocket(srcContent, headers)
                } catch (e: Exception) {
                    Log.e("ClashSingClient", "Base64 decode failed.", e)
                }
            }
            val singBox = subParser?.getSingBox()
            val clashSingContent = if (singBox != null) {
                val subUserinfo = subParser.getSubUserInfo()
                subUserinfo?.let {
                    SubUserinfoManager.setUserinfo(profileId, it)
                }
                customJson.encodeToString(singBox)
            } else {
                throw Exception("Unsupported format.")
            }
            clashSingContent
        }
        if (resultClashSing.isSuccess) {
            return resultClashSing.getOrNull() ?: throw Exception("Response body is null.")
        } else {
            val content = HTTPClient().use { it.getString(url) }
            return content
        }
    }

    private fun getClashSingString(url: String): ClashSingWrapper {
        val request = Request.Builder().url(url)
            .removeHeader("User-Agent")
            .header("User-Agent", getUserAgent())
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
        val csContent = gzipSource.buffer().use<BufferedSource, String> {
            val buffer = Buffer()
            it.readAll(buffer)
            buffer.readString(response.body.contentType()?.charset() ?: Charsets.UTF_8)
        }
        return ClashSingWrapper(csContent, response.headers)
    }

    private fun getUserAgent(): String {
        val defaultUserAgent = try {
            WebSettings.getDefaultUserAgent(Application.Companion.application)
        } catch (e: Exception) {
            System.getProperty("http.agent") ?: ""
        }
        return "$defaultUserAgent $USER_AGENT"
    }

    override fun close() {
        client.dispatcher.executorService.shutdown()
        client.connectionPool.evictAll()
        client.cache?.close()
    }

    private data class ClashSingWrapper(val content: String, val headers: Headers)
}