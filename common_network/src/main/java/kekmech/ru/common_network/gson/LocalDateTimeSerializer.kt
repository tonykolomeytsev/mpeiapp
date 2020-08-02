package kekmech.ru.common_network.gson

import com.google.gson.*
import java.lang.reflect.*
import java.time.*
import java.time.format.*

class LocalDateTimeSerializer : JsonSerializer<LocalDateTime> {

    override fun serialize(src: LocalDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    }
}