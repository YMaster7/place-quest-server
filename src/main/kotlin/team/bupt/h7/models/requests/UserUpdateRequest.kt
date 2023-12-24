package team.bupt.h7.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class UserUpdateRequest(
    val originalPassword: String? = null,
    val password: String? = null,
    val phoneNumber: String? = null,
    val bio: String? = null
)
