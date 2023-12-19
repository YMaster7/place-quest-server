package team.bupt.h7.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class PlaceSeekerQueryParams(
    val userId: Long? = null,
    val destinationTypeList: List<String>? = null,
    val seekerTitlePattern: String? = null,
    val seekerDescriptionPattern: String? = null,
    val maxExpectedPriceRange: Pair<Int?, Int?>? = null,
    val seekerExpiryDateRange: Pair<Instant?, Instant?>? = null,
    val createdAtRange: Pair<Instant?, Instant?>? = null,
    val updatedAtRange: Pair<Instant?, Instant?>? = null,
    val statusList: List<PlaceSeekerStatus>? = null
)
