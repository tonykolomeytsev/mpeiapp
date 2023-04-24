package kekmech.ru.mock_server

import android.content.res.AssetManager
import io.ktor.serialization.gson.gson
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import kekmech.ru.common_network.gson.LocalDateDeserializer
import kekmech.ru.common_network.gson.LocalDateSerializer
import kekmech.ru.common_network.gson.LocalDateTimeSerializer
import kekmech.ru.common_network.gson.LocalTimeDeserializer
import kekmech.ru.common_network.gson.LocalTimeSerializer
import kekmech.ru.mock_server.routing.getScheduleV1
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

private const val Port = 8080
private const val Host = "0.0.0.0"

class MockServer(
    private val assetManager: AssetManager,
    private val logger: MockServerLogger,
) {

    private val server by lazy {
        embeddedServer(
            factory = Netty,
            port = Port,
            host = Host,
            watchPaths = emptyList(),
        ) {
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                    setDateFormat(DateFormat.LONG)
                    registerTypeAdapter(LocalDate::class.java, LocalDateSerializer())
                    registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
                    registerTypeAdapter(LocalTime::class.java, LocalTimeSerializer())
                    registerTypeAdapter(LocalTime::class.java, LocalTimeDeserializer())
                    registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
                }
            }
            routing {
                getScheduleV1()
            }
        }
    }

    fun start() {
        logger.i("Starting mock server on $Host:$Port...")
        server.start(wait = true)
    }
}