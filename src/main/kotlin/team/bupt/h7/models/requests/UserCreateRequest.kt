package team.bupt.h7.models.requests

import kotlinx.serialization.Serializable
import team.bupt.h7.models.entities.DocumentType

@Serializable
data class UserCreateRequest(
    val username: String,
    val password: String,
    val realName: String? = null,
    val documentType: DocumentType? = null,
    val documentNumber: String? = null,
    val phoneNumber: String? = null,
    val bio: String? = null,
    val region: String? = null,
    val district: String? = null,
    val country: String? = null,
)