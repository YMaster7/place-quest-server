package team.bupt.h7.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.cdimascio.dotenv.Dotenv
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val dotenv by inject<Dotenv>()
    val jwtSecret = dotenv["JWT_SECRET"]!!
    authentication {
        jwt {
            // TODO: add issuer and audience
            verifier(
                JWT.require(Algorithm.HMAC256(jwtSecret)).build()
            )
            validate { credential ->
                JWTPrincipal(credential.payload)
            }
        }
    }
}
