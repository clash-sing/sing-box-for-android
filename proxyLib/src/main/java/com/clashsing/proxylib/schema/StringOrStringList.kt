package com.clashsing.proxylib.schema
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

@Serializable
sealed class StringOrStringList {
    @Serializable(with = StringValueSerializer::class) // 明确指定序列化器
    data class StringValue(val value: String) : StringOrStringList()

    @Serializable
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
object StringOrStringListSerializer : JsonContentPolymorphicSerializer<StringOrStringList>(StringOrStringList::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<StringOrStringList> {
        return when (element) {
            is JsonArray -> StringOrStringList.ListValue.serializer()
            is JsonPrimitive -> StringOrStringList.StringValue.serializer()
            else -> throw IllegalArgumentException("Unsupported type for StringOrStringList: ${element::class}")
        }
    }
}