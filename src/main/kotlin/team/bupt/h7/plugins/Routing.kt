package team.bupt.h7.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import team.bupt.h7.routes.userRouting

fun Application.configureRouting() {
    routing {
        route("/api/v1") {
            userRouting()
        }
    }
}
