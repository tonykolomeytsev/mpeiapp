package kekmech.ru.common_network.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LocalTimeJsonAdapter : JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

    override fun serialize(
        src: LocalTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?,
    ): JsonElement {
        return JsonPrimitive(src?.format(DateTimeFormatter.ISO_LOCAL_TIME))
    }

    override fun deserialize(
        json: JsonElement,
        type: Type?,
        jsonDeserializationContext: JsonDeserializationContext?,
    ): LocalTime? {
        return LocalTime.parse(json.asJsonPrimitive.asString)
    }
}
