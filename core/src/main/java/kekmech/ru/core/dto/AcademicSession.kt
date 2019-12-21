package kekmech.ru.core.dto

data class AcademicSession(val events: List<Event>) {

    data class Event(
        val name: String,
        val startTime: String,
        val startDate: String,
        val teacher: String,
        val place: String
    )
}