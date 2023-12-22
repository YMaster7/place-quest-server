package team.bupt.h7.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import team.bupt.h7.routes.placeSeekerRouting
import team.bupt.h7.routes.userRouting
import team.bupt.h7.routes.welcomeOfferRouting

fun Application.configureRouting() {
    routing {
        route("/api/v1") {
            userRouting()
            placeSeekerRouting()
            welcomeOfferRouting()
        }
    }
}
