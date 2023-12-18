package team.bupt.h7.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import team.bupt.h7.exceptions.AuthorizationException

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<AuthorizationException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized, cause.message ?: "Authorization failed.")
        }
    }
}