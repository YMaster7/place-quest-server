package team.bupt.h7.dao

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import org.ktorm.schema.*
import team.bupt.h7.models.entities.DocumentType
import team.bupt.h7.models.entities.User
import team.bupt.h7.models.entities.UserLevel
import team.bupt.h7.models.entities.UserType
import java.time.Instant

class UserDao(private val database: Database) {
    fun createUser(user: User): User {
        database.users.add(user)

        // to retrieve the auto-generated columns
        return database.users.find { it.userId eq user.userId }!!
    }

    fun getUserById(userId: Long): User? {
        return database.users.find { it.userId eq userId }
    }

    fun getUserByUsername(username: String): User? {
        return database.users.find { it.username eq username }
    }

    fun updateUser(user: User): User {
        // update user's updateTime
        user.updateTime = Instant.now()

        database.users.update(user)
        return user
    }

    fun queryUsers(page: Int, pageSize: Int): List<User> {
        val offset = (page - 1) * pageSize
        return database.users.drop(offset).take(pageSize).toList()
    }

    fun getUserCount(): Int {
        return database.users.count()
    }
}

object Users : Table<User>("users") {
    val userId = long("user_id").primaryKey().bindTo { it.userId }
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
    val createTime = timestamp("create_time").bindTo { it.createTime }
    val updateTime = timestamp("update_time").bindTo { it.updateTime }
}

val Database.users get() = this.sequenceOf(Users)
