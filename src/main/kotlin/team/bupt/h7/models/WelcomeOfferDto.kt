package team.bupt.h7.models

import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import kotlinx.serialization.Serializable

@Serializable
data class WelcomeOfferDto(
    val offerId: Long?,
    val userId: Long?,
    val seekerId: Long?,
    val offerDescription: String?,
    val createTime: Instant?,
    val updateTime: Instant?,
    val status: WelcomeOfferStatus?
)

fun WelcomeOffer.toDto() = WelcomeOfferDto(
    offerId = offerId,
    userId = user.id,
    seekerId = seeker.seekerId,
    offerDescription = offerDescription,
    createTime = createTime.toKotlinInstant(),
    updateTime = updateTime.toKotlinInstant(),
    status = status
)
