package team.bupt.h7.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class SeekPlaceDealStatisticQueryParams(
    val startMonth: String,
    val endMonth: String,
    val region: String? = null,
)
