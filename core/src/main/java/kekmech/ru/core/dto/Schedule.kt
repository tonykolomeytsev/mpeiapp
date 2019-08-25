package kekmech.ru.core.dto

data class Schedule(
    var id: Int,
    var group: String,
    var calendarWeek: Int,
    var universityWeek: Int,
    var coupleList: List<CoupleNative>,
    val name: String
)