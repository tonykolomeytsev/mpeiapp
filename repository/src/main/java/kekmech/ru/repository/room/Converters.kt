package kekmech.ru.repository.room

import android.arch.persistence.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun stringToBooleam(value: String?): Boolean? = value.equals("true")

    @TypeConverter
    fun booleanToString(value: Boolean?): String? = if (value == true) "true" else "false"

    @TypeConverter
    fun longToDate(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToLong(value: Date?): Long? = value?.time?.toLong()

    @TypeConverter
    fun intsToString(value: IntArray?): String? = value?.joinToString(",")

    @TypeConverter
    fun stringToInts(value: String?): IntArray? = value?.split(",")?.map { it.toInt() }?.toIntArray()
}