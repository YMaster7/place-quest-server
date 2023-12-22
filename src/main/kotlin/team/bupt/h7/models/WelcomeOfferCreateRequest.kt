package team.bupt.h7.models

import kotlinx.serialization.Serializable

@Serializable
data class WelcomeOfferCreateRequest(
    val seekerId: Long,
    val offerDescription: String
)
