package team.bupt.h7.models.requests

import kotlinx.serialization.Serializable
import team.bupt.h7.models.entities.DocumentType

@Serializable
data class UserCreateRequest(
    val username: String,
    val password: String,
    val realName: String,
    val documentType: DocumentType,
    val documentNumber: String,
    val phoneNumber: String,
    val bio: String? = null,
    val region: String,
    val district: String,
    val country: String,
)