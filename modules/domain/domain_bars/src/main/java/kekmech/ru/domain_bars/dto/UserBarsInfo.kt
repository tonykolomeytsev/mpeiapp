package kekmech.ru.domain_bars.dto

data class UserBarsInfo(
    val name: String? = null,
    val group: String? = null,
    val assessedDisciplines: List<AssessedDiscipline>? = null,
    val rating: Rating? = null,
)