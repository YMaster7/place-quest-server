package team.bupt.h7.models

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import java.time.Instant

interface User : Entity<User> {
    companion object : Entity.Factory<User>()

    val id: Long
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
    var registrationTime: Instant
    var updateTime: Instant
}

enum class UserType {
    NORMAL, ADMIN
}

enum class DocumentType {
    ID_CARD, PASSPORT
}

enum class UserLevel {
    REGULAR, VIP
}

object Users : Table<User>("users") {
    val id = long("id").primaryKey().bindTo { it.id }
    val username = varchar("username").bindTo { it.username }
    val password = varchar("password").bindTo { it.password }
    val userType = enum<UserType>("user_type").bindTo { it.userType }
    val realName = varchar("real_name").bindTo { it.realName }
    val documentType = enum<DocumentType>("document_type").bindTo { it.documentType }
    val documentNumber = varchar("document_number").bindTo { it.documentNumber }
    val phoneNumber = varchar("phone_number").bindTo { it.phoneNumber }
    val userLevel = enum<UserLevel>("user_level").bindTo { it.userLevel }
    val bio = varchar("bio").bindTo { it.bio }
    val region = varchar("region").bindTo { it.region }
    val district = varchar("district").bindTo { it.district }
    val country = varchar("country").bindTo { it.country }
    val registrationTime = timestamp("registration_time").bindTo { it.registrationTime }
    val updateTime = timestamp("update_time").bindTo { it.updateTime }
}

val Database.users get() = this.sequenceOf(Users)
