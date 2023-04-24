package kekmech.ru.common_network.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalTime

class LocalTimeDeserializer : JsonDeserializer<LocalTime> {

    override fun deserialize(
        json: JsonElement,
        type: Type?,
        jsonDeserializationContext: JsonDeserializationContext?
    ): LocalTime? {
        return LocalTime.parse(json.asJsonPrimitive.asString)
    }
}
