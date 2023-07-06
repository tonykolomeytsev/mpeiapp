package kekmech.ru.feature_bars_impl.domain

data class BarsUserInfo(
    val name: String? = null,
    val group: String? = null,
    val assessedDisciplines: List<AssessedDiscipline>? = null,
    val rating: Rating? = null,
)
