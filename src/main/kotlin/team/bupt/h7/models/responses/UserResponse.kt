package team.bupt.h7.models.responses

import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import kotlinx.serialization.Serializable
import team.bupt.h7.models.entities.DocumentType
import team.bupt.h7.models.entities.User
import team.bupt.h7.models.entities.UserLevel
import team.bupt.h7.models.entities.UserType

@Serializable
data class UserResponse(
    val id: Long?,
    val username: String?,
    val password: String?,
    val userType: UserType?,
    val realName: String?,
    val documentType: DocumentType?,
    val documentNumber: String?,
    val phoneNumber: String?,
    val userLevel: UserLevel?,
    val bio: String?,
    val region: String?,
    val district: String?,
    val country: String?,
    val registrationTime: Instant?,
    val updateTime: Instant?
)

fun User.toAdminResponse() = UserResponse(
    id = userId,
    username = username,
    password = null,
    userType = userType,
    realName = realName,
    documentType = documentType,
    documentNumber = documentNumber,
    phoneNumber = phoneNumber,
    userLevel = userLevel,
    bio = bio,
    region = region,
    district = district,
    country = country,
    registrationTime = registrationTime.toKotlinInstant(),
    updateTime = updateTime.toKotlinInstant()
)

fun User.toSelfResponse() = UserResponse(
    id = userId,
    username = username,
    password = null,
    userType = userType,
    realName = realName,
    documentType = documentType,
    documentNumber = documentNumber,
    phoneNumber = phoneNumber,
    userLevel = userLevel,
    bio = bio,
    region = region,
    district = district,
    country = country,
    registrationTime = registrationTime.toKotlinInstant(),
    updateTime = updateTime.toKotlinInstant()
)

fun User.toBasicResponse() = UserResponse(
    id = null,
    username = username,
    password = null,
    userType = null,
    realName = null,
    documentType = null,
    documentNumber = null,
    phoneNumber = null,
    userLevel = userLevel,
    bio = bio,
    region = null,
    district = null,
    country = null,
    registrationTime = null,
    updateTime = null
)