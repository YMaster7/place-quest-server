package team.bupt.h7.utils

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.mindrot.jbcrypt.BCrypt
import team.bupt.h7.exceptions.InsufficientPermissionsException
import team.bupt.h7.exceptions.UserNotFoundException
import team.bupt.h7.models.entities.UserType

fun hashPassword(password: String): String {
    return BCrypt.hashpw(password, BCrypt.gensalt())
}

fun checkPassword(password: String, hashedPassword: String): Boolean {
    return BCrypt.checkpw(password, hashedPassword)
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
