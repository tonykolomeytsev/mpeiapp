package kekmech.ru.common_network.gson

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LocalTimeSerializer : JsonSerializer<LocalTime> {

    override fun serialize(src: LocalTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.format(DateTimeFormatter.ISO_LOCAL_TIME))
    }
}
