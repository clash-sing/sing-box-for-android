package com.clashsing.proxylib.schema
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

@Serializable
sealed class StringOrStringList {
    @Serializable
    data class StringValue(val value: String) : StringOrStringList()

    @Serializable
    data class ListValue(val value: List<String>) : StringOrStringList()
}

object StringOrStringListSerializer : JsonContentPolymorphicSerializer<StringOrStringList>(StringOrStringList::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<StringOrStringList> {
        return when (element) {
            is JsonArray -> StringOrStringList.ListValue.serializer()
            is JsonPrimitive -> StringOrStringList.StringValue.serializer()
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }
}