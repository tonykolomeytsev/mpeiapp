package kekmech.ru.core.dto

import androidx.room.*
import org.jetbrains.annotations.PropertyKey

/**
 * В таком виде приложение хранит информацию о занятиях
 */
@Entity(
//    foreignKeys = [ForeignKey(
//        entity = ScheduleNative::class,
//        parentColumns = ["id"],
//        childColumns = ["schedule_id"])],
    tableName = "couples",
    indices = [Index(
        value = ["id", "schedule_id"],
        unique = true
    )])
class CoupleNative(
//    @PrimaryKey(autoGenerate = true)    var id: Int,
    @ColumnInfo(name = "schedule_id")   var scheduleId: Int,
    @ColumnInfo(name = "name") var name: String,       // название предмета
    @ColumnInfo(name = "teacher") var teacher: String,    // ФИО препода
    @ColumnInfo(name = "place") var place: String,      // место проведения
    @ColumnInfo(name = "time_start")    var timeStart: String,  // время начала
    @ColumnInfo(name = "time_end")      var timeEnd: String,    // время конца
    @ColumnInfo(name = "type") var type: String,       // тип занятия
    @ColumnInfo(name = "num") var num: Int,           // номер пары
    @ColumnInfo(name = "day") var day: Int,           // день проведения
    @ColumnInfo(name = "week") var week: Int           // ODD\EVEN\BOTH
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0

    companion object {
        const val LECTURE = "LECTURE"
        const val PRACTICE = "PRACTICE"
        const val LAB = "LAB"
        const val LUNCH = "LUNCH"
        const val UNKNOWN = "UNKNOWN"
        const val COURSE = "COURSE"
        const val BOTH = 0 // обе недели
        const val ODD = 1 // нечетный
        const val EVEN = 2 // четный
    }
}