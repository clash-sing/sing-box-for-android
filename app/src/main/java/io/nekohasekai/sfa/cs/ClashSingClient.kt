package io.nekohasekai.sfa.cs

import android.util.Log
import android.webkit.WebSettings
import androidx.annotation.WorkerThread
import com.clashsing.proxylib.schema.SingBox
import com.clashsing.proxylib.schema.customJson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.nekohasekai.sfa.Application
import io.nekohasekai.sfa.cs.parser.DefaultSubscriptionParserImpl
import io.nekohasekai.sfa.utils.HTTPClient
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.BufferedSource
import okio.GzipSource
import okio.buffer
import org.yaml.snakeyaml.Yaml
import java.io.Closeable
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import kotlin.io.use

class ClashSingClient(val profileId: Long) : Closeable {
    companion object {
        const val USER_AGENT = "clashmeta/v1.19.12"
        const val ACCEPT = "application/json, application/yaml;q=0.8, text/plain;q=0.5"
        const val ACCEPT_ENCODING = "gzip"
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    @WorkerThread
    fun getString(url: String): String {
//        try {
//            val jsonStr = """{"type": "local", "tag": "system"}"""
//            val dnsServerType = Json.decodeFromString<DnsServerType>(jsonStr)
//            Log.d("ClashSingClient", "dnsServerType: $dnsServerType")
//
//        } catch (e: Exception) {
//            Log.e("ClashSingClient", "",e)
//        }
        val singBoxContent = HTTPClient().use { it.getString(url) }
        try {
            val singBox = customJson.decodeFromString<SingBox>(singBoxContent)
            Log.d("ClashSingClient", "singBox: $singBox")
        } catch (e: Exception) {
            Log.e("ClashSingClient", "",e)
        }
        val singBoxMap = try {
            val type: Type = object : TypeToken<Map<String, Any?>>(){}.type
            Gson().fromJson(singBoxContent, type)
        } catch (e: Exception) {
            emptyMap<String, Any?>()
        }
        val resultWrapper = runCatching {
            getClashSingString(url)
        }
        if (resultWrapper.isSuccess) {
            val mapClashSing = Yaml().load<Map<String, Any?>>(resultWrapper.getOrNull()?.content)
            val parser = DefaultSubscriptionParserImpl(singBoxMap, mapClashSing)
            parser.setSubscriptionUserinfo(profileId, resultWrapper.getOrNull()!!)
            val newContent = parser.getFixedContent()
            return newContent
        } else {
            return singBoxContent
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
            val buffer = okio.Buffer()
            it.readAll(buffer)
            buffer.readString(response.body.contentType()?.charset() ?: Charsets.UTF_8)
        }
        return ClashSingWrapper(csContent.trim(), response.headers)
    }

    private fun getUserAgent(): String {
        val defaultUserAgent = try {
            WebSettings.getDefaultUserAgent(Application.application)
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
}