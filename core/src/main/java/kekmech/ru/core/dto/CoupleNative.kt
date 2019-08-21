package kekmech.ru.core.dto

import io.realm.RealmObject

/**
 * В таком виде приложение хранит информацию о занятиях
 */
open class CoupleNative(
    var name: String? = null,       // название предмета
    var teacher: String? = null,    // ФИО препода
    var place: String? = null,      // место проведения
    var timeStart: String? = null,  // время начала
    var timeEnd: String? = null,    // время конца
    var type: String? = null,       // тип занятия
    var num: Int? = null,           // номер пары
    var day: Int? = null,           // день проведения
    var week: Int? = null           // ODD\EVEN\BOTH
) : RealmObject() {
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