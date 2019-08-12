package kekmech.ru.core.dto

data class Schedule(
    var couples: List<Couple>,
    var groupId: String,
    var semesterId: String
)