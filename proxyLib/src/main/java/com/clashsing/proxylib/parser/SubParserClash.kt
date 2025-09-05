package com.clashsing.proxylib.parser

import android.util.Log
import com.clashsing.proxylib.schema.SingBox
import com.clashsing.proxylib.schema.clash.Clash
import com.clashsing.proxylib.schema.customJson
import com.clashsing.proxylib.schema.decodeFromMap
import okhttp3.Headers
import org.yaml.snakeyaml.Yaml

class SubParserClash(srcContent: String, headers: Headers) : SubParser(srcContent, headers) {
    override fun getSingBox(): SingBox? {
        try {
            val map = Yaml().load<Map<String, *>>(srcContent)
            val clash =customJson.decodeFromMap<Clash>(map)
            Log.d("SubParserClash", "parse clash success")

        } catch (e: Exception) {
            Log.e("SubParserClash", "parse clash failed", e)
        }

        return null
    }

    override fun getSubUserInfo(): String? {
        TODO("Not yet implemented")
    }

}