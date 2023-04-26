package kekmech.ru.mock_server.routing.github

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kekmech.ru.mock_server.randomResponseDelay

internal fun Routing.getContributors() {
    get("/github/repos/{user}/{repo}/stats/contributors") {
        randomResponseDelay()
        call.respond(HttpStatusCode.InternalServerError)
    }
}
