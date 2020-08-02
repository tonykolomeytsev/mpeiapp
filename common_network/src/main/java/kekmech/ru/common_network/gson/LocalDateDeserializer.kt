package kekmech.ru.common_network.gson

import com.google.gson.*
import java.lang.reflect.*
import java.time.*


class LocalDateDeserializer : JsonDeserializer<LocalDate> {

    override fun deserialize(json: JsonElement, type: Type?, jsonDeserializationContext: JsonDeserializationContext?): LocalDate? {
        return LocalDate.parse(json.asJsonPrimitive.asString)
    }
}