package kekmech.ru.addscreen.parser

data class ParserCouple(
    var name: String,       // название предмета
    var teacher: String,    // ФИО препода
    var place: String,      // место проведения
    var timeStart: String,  // время начала
    var timeEnd: String,    // время конца
    var type: String,       // тип занятия
    var num: Int,           // номер пары
    var day: Int,           // день проведения
    var week: Int           // ODD\EVEN\BOTH
) {
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