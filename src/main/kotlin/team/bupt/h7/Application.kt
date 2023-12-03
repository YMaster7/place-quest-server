package team.bupt.h7

import io.ktor.server.application.*
import team.bupt.h7.plugins.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureSecurity()
    configureDatabases()
    configureRouting()
}
