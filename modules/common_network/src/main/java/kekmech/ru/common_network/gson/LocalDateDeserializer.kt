package kekmech.ru.common_network.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDate


class LocalDateDeserializer : JsonDeserializer<LocalDate> {

    override fun deserialize(json: JsonElement, type: Type?, jsonDeserializationContext: JsonDeserializationContext?): LocalDate? {
        return LocalDate.parse(json.asJsonPrimitive.asString)
    }
}