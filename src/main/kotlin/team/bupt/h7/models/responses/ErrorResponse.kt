package team.bupt.h7.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val errorCode: String,
    val errorMessage: String
)
