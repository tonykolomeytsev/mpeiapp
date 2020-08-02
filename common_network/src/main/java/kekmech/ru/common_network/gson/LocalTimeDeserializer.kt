package kekmech.ru.common_network.gson

import com.google.gson.*
import java.lang.reflect.*
import java.time.*
import java.time.format.*

class LocalTimeDeserializer : JsonDeserializer<LocalTime> {

    override fun deserialize(json: JsonElement, type: Type?, jsonDeserializationContext: JsonDeserializationContext?): LocalTime? {
        return LocalTime.parse(json.asJsonPrimitive.asString)
    }
}