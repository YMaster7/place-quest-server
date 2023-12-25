package team.bupt.h7.services.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import team.bupt.h7.models.entities.UserType
import team.bupt.h7.services.AuthService
import java.util.*

class AuthServiceImpl(environment: ApplicationEnvironment) : AuthService {
    private val jwtSecret = environment.config.property("jwt.secret").getString()
    override fun generateJwtToken(userId: Long, userType: UserType): String {
        return JWT.create()
            .withClaim("userId", userId)
            .withClaim("userType", userType.name)
            // TODO: add complete expiring mechanism
            .withExpiresAt(Date(System.currentTimeMillis() + 3600000))    // 1 hour
            .sign(Algorithm.HMAC256(jwtSecret))
    }
}