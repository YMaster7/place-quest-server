package team.bupt.h7.services.impl

import team.bupt.h7.dao.UserDao
import team.bupt.h7.exceptions.*
import team.bupt.h7.models.entities.User
import team.bupt.h7.models.entities.UserLevel
import team.bupt.h7.models.entities.UserType
import team.bupt.h7.models.requests.UserCreateRequest
import team.bupt.h7.models.requests.UserUpdateRequest
import team.bupt.h7.services.UserService
import team.bupt.h7.utils.checkPassword
import team.bupt.h7.utils.hashPassword


class UserServiceImpl(private val userDao: UserDao) : UserService {
    override fun createUser(request: UserCreateRequest): User {
        if (userDao.getUserByUsername(request.username) != null) {
            throw UserAlreadyExistsException()
        }
        val isAdmin = userDao.getUserCount() == 0
        val passwordHash = hashPassword(request.password)
        val user = User {
            username = request.username
            password = passwordHash
            userType = if (isAdmin) UserType.Admin else UserType.Normal
            realName = request.realName
            documentType = request.documentType
            documentNumber = request.documentNumber
            phoneNumber = request.phoneNumber
            userLevel = UserLevel.Regular
            bio = request.bio ?: ""
            region = request.region
            district = request.district
            country = request.country
        }
        return userDao.createUser(user)
    }

    override fun login(userId: Long, password: String): Boolean {
        val user = userDao.getUserById(userId)
            ?: throw UserNotFoundException()
        if (!checkPassword(password, user.password)) {
            throw InvalidPasswordException()
        }
        return true
    }

    override fun updateUser(userId: Long, request: UserUpdateRequest): User {
        val user = userDao.getUserById(userId)
            ?: throw UserNotFoundException()

        // check original password if password is to be updated
        request.password?.let {
            val originalPassword = request.originalPassword
                ?: throw ReauthenticationRequiredException()
            if (!checkPassword(originalPassword, user.password)) {
                throw ReauthInvalidPasswordException()
            }
        }

        user.apply {
            request.password?.let {
                password = hashPassword(it)
            }
            request.phoneNumber?.let {
                phoneNumber = it
            }
            request.bio?.let {
                bio = it
            }
        }

        return userDao.updateUser(user)
    }

    override fun getUserById(userId: Long): User {
        return userDao.getUserById(userId) ?: throw UserNotFoundException()
    }

    override fun getUserByUsername(username: String): User {
        return userDao.getUserByUsername(username) ?: throw UserNotFoundException()
    }

    override fun queryUsers(page: Int, pageSize: Int): List<User> {
        return userDao.queryUsers(page, pageSize)
    }
}
