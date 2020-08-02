package kekmech.ru.common_network.gson

import com.google.gson.*
import java.lang.reflect.*
import java.time.*
import java.time.format.*

class LocalDateSerializer : JsonSerializer<LocalDate> {

    override fun serialize(src: LocalDate?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }
}