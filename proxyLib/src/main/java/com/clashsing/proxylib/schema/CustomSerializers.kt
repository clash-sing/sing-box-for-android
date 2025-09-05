package com.clashsing.proxylib.schema

import com.clashsing.proxylib.BuildConfig
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

// 在您的依赖注入或者 Json 实例提供处
val customJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    explicitNulls = false
    if (BuildConfig.DEBUG) prettyPrint = true // 通常在生产中设为 false 以减少输出大小
    // isLenient = true // 用于处理不严格的 JSON 格式
}

@Serializable
sealed class StringOrStringList {
    @Serializable(with = StringValueSerializer::class) // 明确指定序列化器
    data class StringValue(val value: String) : StringOrStringList()

    @Serializable(with = ListValueSerializer::class)
    data class ListValue(val value: List<String>) : StringOrStringList()
}

object StringValueSerializer : KSerializer<StringOrStringList.StringValue> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("StringOrStringList.StringValue", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: StringOrStringList.StringValue) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): StringOrStringList.StringValue {
        return StringOrStringList.StringValue(decoder.decodeString())
    }
}
object ListValueSerializer : KSerializer<StringOrStringList.ListValue> {
    // 内部使用 List<String> 的默认序列化器
    private val listSerializer = ListSerializer(String.serializer())

    override val descriptor: SerialDescriptor = listSerializer.descriptor // 描述符应该匹配列表的描述符

    override fun serialize(encoder: Encoder, value: StringOrStringList.ListValue) {
        listSerializer.serialize(encoder, value.value)
    }

    override fun deserialize(decoder: Decoder): StringOrStringList.ListValue {
        return StringOrStringList.ListValue(listSerializer.deserialize(decoder))
    }
}
object StringOrStringListSerializer : JsonContentPolymorphicSerializer<StringOrStringList>(StringOrStringList::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<StringOrStringList> {
        return when (element) {
            is JsonArray -> StringOrStringList.ListValue.serializer()
            is JsonPrimitive -> StringOrStringList.StringValue.serializer()
            else -> throw IllegalArgumentException("Unsupported type for StringOrStringList: ${element::class}")
        }
    }
}


fun <T> Json.decodeFromMap(map: Map<String, *>, deserializer: DeserializationStrategy<T>): T {
    val jsonObject = map.toJsonObject()
    return decodeFromJsonElement(deserializer, jsonObject)
}

@OptIn(InternalSerializationApi::class)
fun <T> Json.decodeFromMap(map: Map<String, *>, clazz: KClass<T>): T
        where T : Any {
    return decodeFromMap(map, clazz.serializer())
}

/**
* 将 Map<String, *> 安全转换为 JsonObject，支持嵌套结构
*/
fun Map<String, *>.toJsonObject(): JsonObject = JsonObject(
    mapValues { (_, value) -> value.toJsonElement() }
)

private fun Any?.toJsonElement(): JsonElement = when (this) {
    null -> JsonNull
    is String -> JsonPrimitive(this)
    is Number -> JsonPrimitive(this)
    is Boolean -> JsonPrimitive(this)
    is Char -> JsonPrimitive(this.toString())

    is List<*> -> JsonArray(
        mapNotNull { it.toJsonElement() }
    )

    is Map<*, *> -> {
        // 确保 key 是 String 类型
        @Suppress("UNCHECKED_CAST")
        (this as? Map<String, Any?>)
            ?.toJsonObject()
            ?: JsonPrimitive(toString()) // 备用
    }

    else -> JsonPrimitive(toString())
}

/**
 * 从 Map<String, *> 解码为指定数据类
 */
inline fun <reified T : Any> Json.decodeFromMap(map: Map<String, *>): T =
    decodeFromJsonElement(map.toJsonObject())

inline fun <reified T : Any> Json.decodeFromMap(map: Map<String, *>, noinline block: T.() -> Unit): T =
    decodeFromMap<T>(map).apply(block)