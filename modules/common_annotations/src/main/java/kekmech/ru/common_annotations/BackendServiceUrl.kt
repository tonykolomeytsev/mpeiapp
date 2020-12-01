package kekmech.ru.common_annotations

enum class BackendServiceUrl(
    val prodEndpoint: String,
    val devEndpoint: String
) {
    SCHEDULE(
        prodEndpoint = "https://api.kekmech.com/mpeix/schedule/",
        devEndpoint = "https://dev-api.kekmech.com/mpeix/schedule/"
    ),
    MAP(
        prodEndpoint = "https://api.kekmech.com/mpeix/map/",
        devEndpoint = "https://dev-api.kekmech.com/mpeix/map/"
    )
}