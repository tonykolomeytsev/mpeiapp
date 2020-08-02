package kekmech.ru.common_network.gson

import com.google.gson.*
import java.lang.reflect.*
import java.time.*
import java.time.format.*

class LocalTimeSerializer : JsonSerializer<LocalTime> {

    override fun serialize(src: LocalTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.format(DateTimeFormatter.ISO_LOCAL_TIME))
    }
}