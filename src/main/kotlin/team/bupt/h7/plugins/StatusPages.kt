package team.bupt.h7.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import team.bupt.h7.exceptions.ApplicationException
import team.bupt.h7.models.responses.ErrorResponse

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            val (status, response) = if (cause is ApplicationException) {
                cause.httpStatusCode to cause.toErrorResponse()
            } else {
                HttpStatusCode.InternalServerError to ErrorResponse(
                    "UNKNOWN_ERROR", "An unknown error has occurred on the server."
                )
            }
            call.respond(status, response)
        }
    }
}