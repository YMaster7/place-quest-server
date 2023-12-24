package team.bupt.h7.models.requests

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import team.bupt.h7.models.entities.WelcomeOfferStatus

@Serializable
data class WelcomeOfferQueryParams(
    val userId: Long? = null,
    val seekerId: Long? = null,
    val offerDescriptionPattern: String? = null,
    val createTimeRange: Pair<Instant?, Instant?>? = null,
    val updateTimeRange: Pair<Instant?, Instant?>? = null,
    val statusList: List<WelcomeOfferStatus>? = null
)
