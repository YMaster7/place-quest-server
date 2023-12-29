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
        userId = this["user_id"]?.toLongOrNull(),
        destinationTypeList = this.getAll("destination_type_list"),
        seekerTitlePattern = this["seeker_title_pattern"],
        seekerDescriptionPattern = this["seeker_description_pattern"],
        maxExpectedPriceRange = this["max_expected_price_range"]?.paramToIntRange(),
        seekerExpiryDateRange = this["seeker_expiry_date_range"]?.paramToInstantRange(),
        createdAtRange = this["created_at_range"]?.paramToInstantRange(),
        updatedAtRange = this["updated_at_range"]?.paramToInstantRange(),
        statusList = this.getAll("status_list")?.map { PlaceSeekerStatus.valueOf(it) },
        userRegion = this["user_region"]
    )
}

fun Parameters.toWelcomeOfferQueryParams(): WelcomeOfferQueryParams {
    return WelcomeOfferQueryParams(
        userId = this["user_id"]?.toLongOrNull(),
        seekerId = this["seeker_id"]?.toLongOrNull(),
        offerDescriptionPattern = this["offer_description_pattern"],
        createTimeRange = this["create_time_range"]?.paramToInstantRange(),
        updateTimeRange = this["update_time_range"]?.paramToInstantRange(),
        statusList = this.getAll("status_list")?.map { WelcomeOfferStatus.valueOf(it) }
    )
}

fun Parameters.toSeekPlaceDealStatisticQueryParams(): SeekPlaceDealStatisticQueryParams {
    val startMonth = this["start_month"]
        ?: throw InvalidUrlParametersException()
    val endMonth = this["end_month"]
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
        region = this["region"],
        destinationType = this["destination_type"]
    )
}

fun String.paramToIntRange(): Pair<Int?, Int?>? =
    split(",").takeIf { it.size == 2 }?.let { (a, b) -> a.toIntOrNull() to b.toIntOrNull() }

fun String.paramToInstantRange(): Pair<Instant?, Instant?>? =
    split(",").takeIf { it.size == 2 }?.let { (a, b) -> Instant.parse(a) to Instant.parse(b) }