package kekmech.ru.core.dto

data class AcademicSession(var events: List<Event>) {

    data class Event(
        var name: String = "",
        var startTime: String = "",
        var startDate: String = "",
        var teacher: String = "",
        var place: String = ""
    )
}