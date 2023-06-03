package kekmech.ru.domain_schedule_models

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import kekmech.ru.domain_schedule_models.dto.WeekOfSemester
import java.lang.reflect.Type

private const val MaxWeekNumber = 17
private const val NonStudyingWeekNumber = -1

internal class WeekOfSemesterJsonAdapter :
    JsonDeserializer<WeekOfSemester>,
    JsonSerializer<WeekOfSemester> {

    override fun deserialize(
        json: JsonElement,
        type: Type?,
        jsonDeserializationContext: JsonDeserializationContext?,
    ): WeekOfSemester =
        when (val num = json.asInt) {
            in 0..MaxWeekNumber -> WeekOfSemester.Studying(num)
            else -> WeekOfSemester.NonStudying
        }

    override fun serialize(
        src: WeekOfSemester?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?,
    ): JsonElement =
        JsonPrimitive(
            when (src) {
                is WeekOfSemester.Studying -> src.num
                else -> NonStudyingWeekNumber
            }
        )
}
