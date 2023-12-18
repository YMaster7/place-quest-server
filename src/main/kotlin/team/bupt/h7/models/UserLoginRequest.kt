package team.bupt.h7.models

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginRequest(
    val username: String,
    val password: String
)
