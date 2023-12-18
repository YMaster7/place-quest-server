package team.bupt.h7.services


import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import team.bupt.h7.models.*
import team.bupt.h7.utils.checkPassword
import team.bupt.h7.utils.generateJwtToken
import team.bupt.h7.utils.hashPassword
import java.time.Instant

interface UserService {
    fun register(request: UserRegisterRequest): User
    fun login(userId: Long, password: String): String?
    fun updateUser(userId: Long, request: UserUpdateRequest): User?
    fun getUserById(userId: Long): User?
    fun getUserByUsername(username: String): User?
    fun getPagedUsers(page: Int, pageSize: Int): List<User>
}

class UserServiceImpl(private val database: Database) : UserService {
    override fun register(request: UserRegisterRequest): User {
        if (database.users.find { it.username eq request.username } != null) {
            throw UserAlreadyExistsException()
        }
        val isAdmin = database.users.count() == 0
        val passwordHash = hashPassword(request.password)
        val user = User {
            username = request.username
            password = passwordHash
            userType = if (isAdmin) UserType.ADMIN else UserType.NORMAL
            realName = request.realName ?: ""
            documentType = request.documentType ?: DocumentType.ID_CARD
            documentNumber = request.documentNumber ?: "0000"
            phoneNumber = request.phoneNumber ?: "0000"
            userLevel = UserLevel.REGULAR
            bio = request.bio ?: ""
            region = request.region ?: ""
            district = request.district ?: ""
            country = request.country ?: ""
            registrationTime = Instant.now()
            updateTime = Instant.now()
        }
        database.users.add(user)
        return user
    }

    override fun login(userId: Long, password: String): String? {
        val user = database.users.find { it.id eq userId } ?: return null
        if (checkPassword(password, user.password)) {
            return generateJwtToken(user.id, user.userType)
        }
        return null
    }

    override fun updateUser(userId: Long, request: UserUpdateRequest): User? {
        val user = database.users.find { it.id eq userId } ?: return null
        user.apply {
            request.password?.let { password = hashPassword(it) }
            request.phoneNumber?.let { phoneNumber = it }
            request.bio?.let { bio = it }
            updateTime = Instant.now()
        }
        database.users.update(user)
        return user
    }

    override fun getUserById(userId: Long): User? {
        return database.users.find { it.id eq userId }
    }

    override fun getUserByUsername(username: String): User? {
        return database.users.find { it.username eq username }
    }

    override fun getPagedUsers(page: Int, pageSize: Int): List<User> {
        val offset = (page - 1) * pageSize
        return database.users.drop(offset).take(pageSize).toList()
    }
}

class UserAlreadyExistsException : Exception("User already exists")
