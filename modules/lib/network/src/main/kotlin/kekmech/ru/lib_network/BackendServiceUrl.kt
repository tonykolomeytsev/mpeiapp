package kekmech.ru.lib_network

@Suppress("MaxLineLength")
public enum class BackendServiceUrl(
    public val prodEndpoint: String,
    public val stagingEndpoint: String,
    public val mockEndpoint: String,
) {
    SCHEDULE(
        prodEndpoint = "https://api.kekmech.com/mpeix/schedule/",
        stagingEndpoint = "https://dev-api.kekmech.com/mpeix/schedule/",
        mockEndpoint = "http://localhost:8080/schedule/",
    ),
    MPEI_TIMETABLE(
        prodEndpoint = "https://ts.mpei.ru/api/",
        stagingEndpoint = "https://ts.mpei.ru/api/",
        mockEndpoint = "http://localhost:8080/api/",
    ),
    MAP(
        prodEndpoint = "https://raw.githubusercontent.com/ghostoftheeleven/mpei-next/master/statics/map/",
        stagingEndpoint = "https://raw.githubusercontent.com/ghostoftheeleven/mpei-next/dev/statics/map/",
        mockEndpoint = "http://localhost:8080/map/",
    ),
    BARS(
        prodEndpoint = "https://raw.githubusercontent.com/ghostoftheeleven/mpei-next/master/statics/bars/",
        stagingEndpoint = "https://raw.githubusercontent.com/ghostoftheeleven/mpei-next/dev/statics/bars/",
        mockEndpoint = "http://localhost:8080/bars/",
    ),
    GITHUB(
        prodEndpoint = "https://api.github.com/",
        stagingEndpoint = "https://api.github.com/",
        mockEndpoint = "http://localhost:8080/github/",
    )
}
