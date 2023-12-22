package team.bupt.h7.models

import kotlinx.serialization.Serializable

@Serializable
data class WelcomeOfferUpdateRequest(
    val offerDescription: String? = null,
)
