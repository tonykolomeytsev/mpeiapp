package kekmech.ru.mock_server

interface MockServerStaticsProvider {

    fun provide(path: String): String
}
