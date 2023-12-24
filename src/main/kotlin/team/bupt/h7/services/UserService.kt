package team.bupt.h7.services


import team.bupt.h7.models.entities.User
import team.bupt.h7.models.requests.UserCreateRequest
import team.bupt.h7.models.requests.UserUpdateRequest

interface UserService {
    fun createUser(request: UserCreateRequest): User
    fun login(userId: Long, password: String): String
    fun getUserById(userId: Long): User
    fun getUserByUsername(username: String): User
    fun updateUser(userId: Long, request: UserUpdateRequest): User
    fun queryUsers(page: Int, pageSize: Int): List<User>
}
