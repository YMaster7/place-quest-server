package team.bupt.h7.models.responses

import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import kotlinx.serialization.Serializable
import team.bupt.h7.models.entities.PlaceSeeker
import team.bupt.h7.models.entities.PlaceSeekerStatus

@Serializable
data class PlaceSeekerResponse(
    val seekerId: Long?,
    val userId: Long?,
    val username: String?,
    val userRegion: String?,
    val destinationType: String?,
    val seekerTitle: String?,
    val seekerDescription: String?,
    val attachmentUrl: String?,
    val maxExpectedPrice: Int?,
    val seekerExpiryDate: Instant?,
    val createTime: Instant?,
    val updateTime: Instant?,
    val status: PlaceSeekerStatus?
)

fun PlaceSeeker.toResponse() = PlaceSeekerResponse(
    seekerId = seekerId,
    userId = user.userId,
    username = user.username,
    userRegion = user.region,
    destinationType = destinationType,
    seekerTitle = seekerTitle,
    seekerDescription = seekerDescription,
    attachmentUrl = attachmentUrl,
    maxExpectedPrice = maxExpectedPrice,
    seekerExpiryDate = seekerExpiryDate.toKotlinInstant(),
    createTime = createTime.toKotlinInstant(),
    updateTime = updateTime.toKotlinInstant(),
    status = status
)
