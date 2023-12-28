package team.bupt.h7.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.forwardedheaders.*

fun Application.configureForwardedHeader() {
    install(XForwardedHeaders)
}