package team.bupt.h7.models.responses

import kotlinx.serialization.Serializable
import team.bupt.h7.models.entities.SeekPlaceDealStatistic

@Serializable
data class SeekPlaceDealStatisticsResponse(
    val yearMonth: String,
    val totalDeals: Int,
    val totalBrokerage: Int,
)

fun SeekPlaceDealStatistic.toResponse() = SeekPlaceDealStatisticsResponse(
    yearMonth = yearMonth,
    totalDeals = totalDeals,
    totalBrokerage = totalBrokerage,
)
