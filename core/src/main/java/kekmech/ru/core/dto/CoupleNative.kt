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
    var day: Int? = null            // день проведения
) : RealmObject()