package kekmech.ru.core.dto

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

/**
 * В таком виде приложение хранит информацию о занятиях
 */
@Entity(
    foreignKeys = [ForeignKey(
        entity = ScheduleNative::class,
        parentColumns = ["id"],
        childColumns = ["schedule_id"],
        onDelete = ForeignKey.CASCADE)],
    tableName = "couples")
open class CoupleNative(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "schedule_id") var scheduleId: Int = 0,
    var name: String? = null,       // название предмета
    var teacher: String? = null,    // ФИО препода
    var place: String? = null,      // место проведения
    @ColumnInfo(name = "time_start") var timeStart: String? = null,  // время начала
    @ColumnInfo(name = "time_end") var timeEnd: String? = null,    // время конца
    var type: String? = null,       // тип занятия
    var num: Int? = null,           // номер пары
    var day: Int? = null,           // день проведения
    var week: Int? = null           // ODD\EVEN\BOTH
) {
    companion object {
        const val LECTURE = "LECTURE"
        const val PRACTICE = "PRACTICE"
        const val LAB = "LAB"
        const val LUNCH = "LUNCH"
        const val UNKNOWN = "UNKNOWN"
        const val BOTH = 0 // обе недели
        const val ODD = 1 // нечетный
        const val EVEN = 2 // четный
    }
}