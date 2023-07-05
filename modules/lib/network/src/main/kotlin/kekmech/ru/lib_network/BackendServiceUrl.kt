package kekmech.ru.lib_network

@Suppress("MaxLineLength")
enum class BackendServiceUrl(
    val prodEndpoint: String,
    val stagingEndpoint: String,
    val mockEndpoint: String,
) {
    SCHEDULE(
        prodEndpoint = "https://api.kekmech.com/mpeix/schedule/",
        stagingEndpoint = "https://dev-api.kekmech.com/mpeix/schedule/",
        mockEndpoint = "http://localhost:8080/schedule/",
    ),
    MAP(
        prodEndpoint = "https://raw.githubusercontent.com/tonykolomeytsev/mpeiapp/master/statics/map/",
        stagingEndpoint = "https://raw.githubusercontent.com/tonykolomeytsev/mpeiapp/dev/statics/map/",
        mockEndpoint = "http://localhost:8080/map/",
    ),
    BARS(
        prodEndpoint = "https://raw.githubusercontent.com/tonykolomeytsev/mpeiapp/master/statics/bars/",
        stagingEndpoint = "https://raw.githubusercontent.com/tonykolomeytsev/mpeiapp/dev/statics/bars/",
        mockEndpoint = "http://localhost:8080/bars/",
    ),
    GITHUB(
        prodEndpoint = "https://api.github.com/",
        stagingEndpoint = "https://api.github.com/",
        mockEndpoint = "http://localhost:8080/github/",
    )
}
