package kekmech.ru.ext_gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateJsonAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    override fun serialize(
        src: LocalDate?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?,
    ): JsonElement {
        return JsonPrimitive(src?.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }

    override fun deserialize(
        json: JsonElement,
        type: Type?,
        jsonDeserializationContext: JsonDeserializationContext?,
    ): LocalDate? {
        return LocalDate.parse(json.asJsonPrimitive.asString)
    }
}
