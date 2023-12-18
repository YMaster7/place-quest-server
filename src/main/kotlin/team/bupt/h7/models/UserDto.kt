package team.bupt.h7.models

import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
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

fun User.toAdminDto() = UserDto(
    id = id,
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

fun User.toSelfDto() = UserDto(
    id = id,
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

fun User.toBasicDto() = UserDto(
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