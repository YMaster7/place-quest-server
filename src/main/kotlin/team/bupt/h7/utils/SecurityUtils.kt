package team.bupt.h7.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.mindrot.jbcrypt.BCrypt
import team.bupt.h7.exceptions.InsufficientPermissionsException
import team.bupt.h7.exceptions.UserNotFoundException
import team.bupt.h7.models.entities.UserType
import java.util.*

fun hashPassword(password: String): String {
    return BCrypt.hashpw(password, BCrypt.gensalt())
}

fun checkPassword(password: String, hashedPassword: String): Boolean {
    return BCrypt.checkpw(password, hashedPassword)
}

fun generateJwtToken(userId: Long, userType: UserType): String {
    // TODO: jwt service
    val dotenv = dotenv()
    val jwtSecret = dotenv["JWT_SECRET"]!!

    return JWT.create()
        .withClaim("userId", userId)
        .withClaim("userType", userType.name)
        // TODO: add complete expiring mechanism
        .withExpiresAt(Date(System.currentTimeMillis() + 3600000))    // 1 hour
        .sign(Algorithm.HMAC256(jwtSecret))
}

fun ApplicationCall.getUserIdFromToken(): Long {
    val userId = principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()
        ?: throw UserNotFoundException()
    return userId
}

fun ApplicationCallPipeline.roleCheck(requiredRole: UserType) {
    intercept(ApplicationCallPipeline.Call) {
        val principal = call.principal<JWTPrincipal>()
        val userType =
            principal?.payload?.getClaim("userType")?.asString()?.let { UserType.valueOf(it) }
        if (userType != requiredRole) {
            throw InsufficientPermissionsException()
        }
    }
}
