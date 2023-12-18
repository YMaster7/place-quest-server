package team.bupt.h7.models

import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterRequest(
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