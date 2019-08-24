package kekmech.ru.core.dto

import android.arch.persistence.room.*

/**
 * В таком виде приложение хранит информацию о занятиях
 */
@Entity(
    foreignKeys = [ForeignKey(
        entity = ScheduleNative::class,
        parentColumns = ["id"],
        childColumns = ["schedule_id"])],
    tableName = "couples",
    indices = [Index(
        value = ["id", "schedule_id"],
        unique = true
    )])
open class CoupleNative @Ignore constructor(
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
    constructor(): this(0) // Чтобы скрыть WARNING при компиляции

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