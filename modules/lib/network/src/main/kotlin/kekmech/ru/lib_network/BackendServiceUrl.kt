package kekmech.ru.lib_network

/**
 * Каталог базовых URL-адресов сетевых сервисов приложения «МЭИ.Хаб».
 *
 * Поддерживает три контура развертывания (Production, Staging, Mock)
 * для удобного переключения и тестирования сетевого слоя.
 */
@Suppress("MaxLineLength")
public enum class BackendServiceUrl(
    public val prodEndpoint: String,
    public val stagingEndpoint: String,
    public val mockEndpoint: String,
) {
    /** Легаси-сервер расписания MpeiX (сохранен для совместимости) */
    SCHEDULE(
        prodEndpoint = "https://api.kekmech.com/mpeix/schedule/",
        stagingEndpoint = "https://dev-api.kekmech.com/mpeix/schedule/",
        mockEndpoint = "http://localhost:8080/schedule/",
    ),
    /** Официальный прямой REST API расписания НИУ «МЭИ» */
    MPEI_TIMETABLE(
        prodEndpoint = "https://ts.mpei.ru/api/",
        stagingEndpoint = "https://ts.mpei.ru/api/",
        mockEndpoint = "http://localhost:8080/api/",
    ),
    /** Статические геометки и контуры зданий кампуса OpenStreetMap */
    MAP(
        prodEndpoint = "https://raw.githubusercontent.com/ghostoftheeleven/mpei-next/master/statics/map/",
        stagingEndpoint = "https://raw.githubusercontent.com/ghostoftheeleven/mpei-next/dev/statics/map/",
        mockEndpoint = "http://localhost:8080/map/",
    ),
    /** Конфигурация и JS-скрипты парсинга личного кабинета БАРС */
    BARS(
        prodEndpoint = "https://raw.githubusercontent.com/ghostoftheeleven/mpei-next/master/statics/bars/",
        stagingEndpoint = "https://raw.githubusercontent.com/ghostoftheeleven/mpei-next/dev/statics/bars/",
        mockEndpoint = "http://localhost:8080/bars/",
    ),
    /** Публичный GitHub REST API (контрибьюторы, релизы, обновления) */
    GITHUB(
        prodEndpoint = "https://api.github.com/",
        stagingEndpoint = "https://api.github.com/",
        mockEndpoint = "http://localhost:8080/github/",
    )
}
