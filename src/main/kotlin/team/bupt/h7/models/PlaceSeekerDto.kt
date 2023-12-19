package team.bupt.h7.models

import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import kotlinx.serialization.Serializable

@Serializable
data class PlaceSeekerDto(
    val seekerId: Long?,
    val userId: Long?,
    val destinationType: String?,
    val seekerTitle: String?,
    val seekerDescription: String?,
    val maxExpectedPrice: Int?,
    val seekerExpiryDate: Instant?,
    val createTime: Instant?,
    val updateTime: Instant?,
    val status: PlaceSeekerStatus?
)

fun PlaceSeeker.toDto() = PlaceSeekerDto(
    seekerId = seekerId,
    userId = user.id,
    destinationType = destinationType,
    seekerTitle = seekerTitle,
    seekerDescription = seekerDescription,
    maxExpectedPrice = maxExpectedPrice,
    seekerExpiryDate = seekerExpiryDate.toKotlinInstant(),
    createTime = createTime.toKotlinInstant(),
    updateTime = updateTime.toKotlinInstant(),
    status = status
)
