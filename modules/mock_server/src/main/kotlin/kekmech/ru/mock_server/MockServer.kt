package kekmech.ru.mock_server

import android.content.res.AssetManager
import io.ktor.serialization.gson.gson
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import kekmech.ru.ext_gson.LocalDateJsonAdapter
import kekmech.ru.ext_gson.LocalDateTimeJsonAdapter
import kekmech.ru.ext_gson.LocalTimeJsonAdapter
import kekmech.ru.mock_server.routing.bars.getExtractJs
import kekmech.ru.mock_server.routing.bars.getRemoteBarsConfig
import kekmech.ru.mock_server.routing.github.getContributors
import kekmech.ru.mock_server.routing.github.getUser
import kekmech.ru.mock_server.routing.map.getMapMarkers
import kekmech.ru.mock_server.routing.schedule.getScheduleV1
import kekmech.ru.mock_server.routing.schedule.getSearchResultsV1
import timber.log.Timber
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

private const val Port = 8080
private const val Host = "0.0.0.0"

/**
 * # Mock Server
 *
 * MockServer is a server that runs on the local network and can emulate the behavior of the
 * application's backend. MockServer blocks the thread it runs on, so run it on a dedicated thread.
 *
 * ## Usage:
 * ```kotlin
 * val mockServer = MockServer(assetManager = context.assets)
 * mockServer.start() // blocking call
 * ```
 *
 * @param assetManager [AssetManager] for accessing static resources (stubs)
 * located in the assets directory
 */
class MockServer(
    private val assetManager: AssetManager,
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
                    registerTypeAdapter(LocalDate::class.java, LocalDateJsonAdapter())
                    registerTypeAdapter(LocalTime::class.java, LocalTimeJsonAdapter())
                    registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeJsonAdapter())
                }
            }
            routing {
                // region schedule
                getScheduleV1()
                getSearchResultsV1()
                // end region

                // region map
                getMapMarkers(assetManager)
                // end region

                // region bars
                getExtractJs(assetManager)
                getRemoteBarsConfig(assetManager)
                // end region

                // region github
                getContributors()
                getUser()
                // end region
            }
        }
    }

    /**
     * Start MockServer.
     *
     * This call is blocking. Don't run MockServer on the main application thread!
     */
    fun start() {
        Timber.i("Starting mock server on $Host:$Port...")
        server.start(wait = true)
    }
}
