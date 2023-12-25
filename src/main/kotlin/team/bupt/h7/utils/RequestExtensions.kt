package team.bupt.h7.utils

import io.ktor.http.*
import kotlinx.datetime.Instant
import team.bupt.h7.exceptions.InvalidUrlParametersException
import team.bupt.h7.models.entities.PlaceSeekerStatus
import team.bupt.h7.models.entities.WelcomeOfferStatus
import team.bupt.h7.models.requests.PlaceSeekerQueryParams
import team.bupt.h7.models.requests.SeekPlaceDealStatisticQueryParams
import team.bupt.h7.models.requests.WelcomeOfferQueryParams
import java.time.YearMonth
import java.time.format.DateTimeParseException

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

fun Parameters.toWelcomeOfferQueryParams(): WelcomeOfferQueryParams {
    return WelcomeOfferQueryParams(
        userId = this["userId"]?.toLongOrNull(),
        seekerId = this["seekerId"]?.toLongOrNull(),
        offerDescriptionPattern = this["offerDescriptionPattern"],
        createTimeRange = this["createTimeRange"]?.paramToInstantRange(),
        updateTimeRange = this["updateTimeRange"]?.paramToInstantRange(),
        statusList = this.getAll("statusList")?.map { WelcomeOfferStatus.valueOf(it) }
    )
}

fun Parameters.toSeekPlaceDealStatisticQueryParams(): SeekPlaceDealStatisticQueryParams {
    val startMonth = this["startMonth"]
        ?: throw InvalidUrlParametersException()
    val endMonth = this["endMonth"]
        ?: throw InvalidUrlParametersException()
    try {
        YearMonth.parse(startMonth)
        YearMonth.parse(endMonth)
    } catch (e: DateTimeParseException) {
        throw InvalidUrlParametersException()
    }

    return SeekPlaceDealStatisticQueryParams(
        startMonth = startMonth,
        endMonth = endMonth,
        region = this["region"]
    )
}

fun String.paramToIntRange(): Pair<Int?, Int?>? =
    split(",").takeIf { it.size == 2 }?.let { (a, b) -> a.toIntOrNull() to b.toIntOrNull() }

fun String.paramToInstantRange(): Pair<Instant?, Instant?>? =
    split(",").takeIf { it.size == 2 }?.let { (a, b) -> Instant.parse(a) to Instant.parse(b) }