package team.bupt.h7.models.requests

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import team.bupt.h7.models.entities.PlaceSeekerStatus

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
    val statusList: List<PlaceSeekerStatus>? = null,
    val userRegion: String? = null,
)
