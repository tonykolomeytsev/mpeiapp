package kekmech.ru.domain_app_settings.dto

data class ContributorsItem(
    val total: Long,
    val author: Author
)

data class Author(
    val login: String,
)
