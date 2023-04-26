package kekmech.ru.mock_server.routing.bars

import android.content.res.AssetManager
import io.ktor.http.ContentType
import io.ktor.server.application.call
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kekmech.ru.mock_server.randomResponseDelay

internal fun Routing.getExtractJs(assetManager: AssetManager) {
    get("/bars/extract.js") {
        randomResponseDelay()
        call.respondBytes(
            bytes = assetManager.open("github/extract.js").readBytes(),
            contentType = ContentType.Application.Json,
        )
    }
}
