package kekmech.ru.domain_bars.dto

public data class SemesterInfo(
    val currentSemesterId: String,
    val allSemesters: List<SemesterEntry>
)

public data class SemesterEntry(
    val id: String,
    val name: String
)
