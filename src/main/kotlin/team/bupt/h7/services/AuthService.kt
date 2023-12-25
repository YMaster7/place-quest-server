package team.bupt.h7.services

import team.bupt.h7.models.entities.UserType

interface AuthService {
    fun generateJwtToken(userId: Long, userType: UserType): String
}