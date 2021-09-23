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
        prodEndpoint = "https://raw.githubusercontent.com/tonykolomeytsev/mpeiapp/master/statics/map/",
        devEndpoint = "https://raw.githubusercontent.com/tonykolomeytsev/mpeiapp/dev/statics/map/"
    ),
    BARS(
        prodEndpoint = "https://raw.githubusercontent.com/tonykolomeytsev/mpeiapp/master/statics/bars/",
        devEndpoint = "https://raw.githubusercontent.com/tonykolomeytsev/mpeiapp/dev/statics/bars/"
    ),
    GITHUB(
        prodEndpoint = "https://api.github.com/",
        devEndpoint = "https://api.github.com/"
    )
}