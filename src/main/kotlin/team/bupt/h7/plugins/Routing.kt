package team.bupt.h7.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get
import team.bupt.h7.routes.*

fun Application.configureRouting() {
    routing {
        route("/api/v1") {
            userRouting(get(), get())
            placeSeekerRouting(get())
            welcomeOfferRouting(get())
            seekPlaceDealRouting(get())
            fileRouting()
        }
    }
}
