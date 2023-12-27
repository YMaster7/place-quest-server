package team.bupt.h7.models.responses

import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import kotlinx.serialization.Serializable
import team.bupt.h7.models.entities.WelcomeOffer
import team.bupt.h7.models.entities.WelcomeOfferStatus

@Serializable
data class WelcomeOfferResponse(
    val offerId: Long?,
    val userId: Long?,
    val username: String?,
    val seekerId: Long?,
    val offerDescription: String?,
    val createTime: Instant?,
    val updateTime: Instant?,
    val status: WelcomeOfferStatus?
)

fun WelcomeOffer.toResponse() = WelcomeOfferResponse(
    offerId = offerId,
    userId = user.userId,
    username = user.username,
    seekerId = seeker.seekerId,
    offerDescription = offerDescription,
    createTime = createTime.toKotlinInstant(),
    updateTime = updateTime.toKotlinInstant(),
    status = status
)
