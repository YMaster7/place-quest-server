package team.bupt.h7.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class WelcomeOfferUpdateRequest(
    val offerDescription: String? = null,
)
