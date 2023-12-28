package team.bupt.h7.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import kotlin.time.Duration.Companion.seconds

fun Application.configureRateLimit() {
    install(RateLimit) {
        global {
            rateLimiter(limit = 200, refillPeriod = 60.seconds)
            requestKey {
                it.request.header("CF-Connecting-IP") ?: it.request.origin.remoteHost
            }
        }
    }
}