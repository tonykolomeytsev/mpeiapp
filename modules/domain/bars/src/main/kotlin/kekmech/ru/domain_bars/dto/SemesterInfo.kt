package kekmech.ru.domain_bars.dto

data class SemesterInfo(
    val currentSemesterId: String,
    val allSemesters: List<SemesterEntry>
)

data class SemesterEntry(
    val id: String,
    val name: String
)
