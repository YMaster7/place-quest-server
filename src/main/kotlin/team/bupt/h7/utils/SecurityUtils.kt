package team.bupt.h7.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.cdimascio.dotenv.dotenv
import org.mindrot.jbcrypt.BCrypt
import team.bupt.h7.models.UserType
import java.util.*

private val jwtSecret = dotenv()["JWT_SECRET"] ?: error("JWT_SECRET not specified")

fun hashPassword(password: String): String {
    return BCrypt.hashpw(password, BCrypt.gensalt())
}

fun checkPassword(password: String, hashedPassword: String): Boolean {
    return BCrypt.checkpw(password, hashedPassword)
}

fun generateJwtToken(userId: Long, userType: UserType): String {
    return JWT.create()
        .withClaim("userId", userId)
        .withClaim("userType", userType.name)
        // TODO: add complete expiring mechanism
        .withExpiresAt(Date(System.currentTimeMillis() + 3600000))    // 1 hour
        .sign(Algorithm.HMAC256(jwtSecret))
}
