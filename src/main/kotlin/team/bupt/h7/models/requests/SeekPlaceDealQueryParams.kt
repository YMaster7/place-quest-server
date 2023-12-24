package team.bupt.h7.models.requests

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class SeekPlaceDealQueryParams(
    val seekerId: Long? = null,
    val offerId: Long? = null,
    val seekerPriceRange: Pair<Int?, Int?>? = null,
    val offerPriceRange: Pair<Int?, Int?>? = null,
    val createTimeRange: Pair<Instant?, Instant?>? = null
)
