package team.bupt.h7.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class WelcomeOfferCreateRequest(
    val seekerId: Long,
    val offerDescription: String,
    val attachmentUrl: String = ""
)
