package team.bupt.h7.utils

import io.ktor.http.*
import kotlinx.datetime.Instant
import team.bupt.h7.models.PlaceSeekerQueryParams
import team.bupt.h7.models.PlaceSeekerStatus

fun Parameters.toPlaceSeekerQueryParams(): PlaceSeekerQueryParams {
    return PlaceSeekerQueryParams(
        userId = this["userId"]?.toLongOrNull(),
        destinationTypeList = this.getAll("destinationTypeList"),
        seekerTitlePattern = this["seekerTitlePattern"],
        seekerDescriptionPattern = this["seekerDescriptionPattern"],
        maxExpectedPriceRange = this["maxExpectedPriceRange"]?.paramToIntRange(),
        seekerExpiryDateRange = this["seekerExpiryDateRange"]?.paramToInstantRange(),
        createdAtRange = this["createdAtRange"]?.paramToInstantRange(),
        updatedAtRange = this["updatedAtRange"]?.paramToInstantRange(),
        statusList = this.getAll("statusList")?.map { PlaceSeekerStatus.valueOf(it) }
    )
}

fun String.paramToIntRange(): Pair<Int?, Int?>? =
    split(",").takeIf { it.size == 2 }?.let { (a, b) -> a.toIntOrNull() to b.toIntOrNull() }

fun String.paramToInstantRange(): Pair<Instant?, Instant?>? =
    split(",").takeIf { it.size == 2 }?.let { (a, b) -> Instant.parse(a) to Instant.parse(b) }