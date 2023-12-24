package team.bupt.h7.models.requests

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class PlaceSeekerUpdateRequest(
    val destinationType: String? = null,
    val seekerTitle: String? = null,
    val seekerDescription: String? = null,
    val maxExpectedPrice: Int? = null,
    val seekerExpiryDate: Instant? = null,
)
