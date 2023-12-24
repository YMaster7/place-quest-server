package team.bupt.h7.models.entities

import org.ktorm.entity.Entity
import java.time.Instant

interface User : Entity<User> {
    companion object : Entity.Factory<User>()

    val userId: Long
    var username: String
    var password: String
    var userType: UserType
    var realName: String
    var documentType: DocumentType
    var documentNumber: String
    var phoneNumber: String
    var userLevel: UserLevel
    var bio: String
    var region: String
    var district: String
    var country: String
    var createTime: Instant
    var updateTime: Instant
}

enum class UserType {
    Normal, Admin
}

enum class DocumentType {
    IdCard, Passport
}

enum class UserLevel {
    Regular, Vip
}
