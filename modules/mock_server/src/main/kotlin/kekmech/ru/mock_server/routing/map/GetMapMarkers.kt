package kekmech.ru.mock_server.routing.map

import android.content.res.AssetManager
import io.ktor.http.ContentType
import io.ktor.server.application.call
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kekmech.ru.mock_server.randomResponseDelay

internal fun Routing.getMapMarkers(assetManager: AssetManager) {
    get("/map/markers.json") {
        randomResponseDelay()
        call.respondBytes(
            bytes = assetManager.open("github/markers.json").readBytes(),
            contentType = ContentType.Application.Json,
        )
    }
}
