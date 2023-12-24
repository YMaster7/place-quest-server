package team.bupt.h7.models.responses

import kotlinx.serialization.Serializable
import team.bupt.h7.models.entities.UserType

@Serializable
data class LoginResponse(
    val token: String,
    val userType: UserType,
)