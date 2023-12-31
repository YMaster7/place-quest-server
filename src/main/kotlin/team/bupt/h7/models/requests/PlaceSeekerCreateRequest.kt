package team.bupt.h7.models.requests

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class PlaceSeekerCreateRequest(
    val destinationType: String,
    val seekerTitle: String,
    val seekerDescription: String,
    val attachmentUrl: String = "",
    val maxExpectedPrice: Int,
    val seekerExpiryDate: Instant
)
