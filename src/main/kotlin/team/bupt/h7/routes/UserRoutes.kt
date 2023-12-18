package team.bupt.h7.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import team.bupt.h7.exceptions.AuthorizationException
import team.bupt.h7.models.*
import team.bupt.h7.services.UserAlreadyExistsException
import team.bupt.h7.services.UserService
import team.bupt.h7.utils.checkPassword

fun Route.userRouting() {
    val userService: UserService by inject()

    route("/users") {
        post("/register") {
            val request = call.receive<UserRegisterRequest>()
            val user = try {
                userService.register(request)
            } catch (e: UserAlreadyExistsException) {
                call.respond(HttpStatusCode.Conflict, "User already exists.")
                return@post
            }
            call.respond(HttpStatusCode.Created, user.toSelfDto())
        }

        post("/login") {
            val request = call.receive<UserLoginRequest>()
            val user =
                userService.getUserByUsername(request.username) ?: throw AuthorizationException()
            val token =
                userService.login(user.id, request.password) ?: throw AuthorizationException()
            call.respond(HttpStatusCode.OK, token)
        }

        authenticate {
            get("/me") {
                val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()
                    ?: throw AuthorizationException()
                val user = userService.getUserById(userId) ?: throw AuthorizationException()
                call.respond(HttpStatusCode.OK, user.toSelfDto())
            }

            put("/me") {
                val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()
                    ?: throw AuthorizationException()
                val request = call.receive<UserUpdateRequest>()

                request.password?.let {
                    val user = userService.getUserById(userId) ?: throw AuthorizationException()
                    val originalPassword = request.originalPassword ?: throw AuthorizationException(
                        "Original password is required."
                    )
                    if (!checkPassword(originalPassword, user.password)) {
                        throw AuthorizationException("Old password is incorrect.")
                    }
                }

                val user = userService.updateUser(userId, request)
                    ?: throw NotFoundException("User not found.")
                call.respond(HttpStatusCode.OK, user.toSelfDto())
            }

            // TODO: find other users
        }
    }

    route("/admin/users") {
        authenticate {
            roleCheck(UserType.ADMIN)

            get("/") {
                val page = call.parameters["page"]?.toIntOrNull()
                    ?: throw BadRequestException("Invalid page number.")
                val pageSize = call.parameters["pageSize"]?.toIntOrNull()
                    ?: throw BadRequestException("Invalid page size.")

                if (page <= 0 || pageSize <= 0) {
                    throw BadRequestException("Page number and page size must be greater than 0.")
                }

                val users = userService.getPagedUsers(page, pageSize)
                call.respond(HttpStatusCode.OK, users.map { it.toAdminDto() })
            }

            get("/username/{username}") {
                val username =
                    call.parameters["username"] ?: throw BadRequestException("Invalid username.")
                val user = userService.getUserByUsername(username)
                    ?: throw NotFoundException("User not found.")
                call.respond(HttpStatusCode.OK, user.toAdminDto())
            }

            get("/{userId}") {
                val userId = call.parameters["userId"]?.toLongOrNull() ?: throw BadRequestException(
                    "Invalid user ID."
                )
                val user =
                    userService.getUserById(userId) ?: throw NotFoundException("User not found.")
                call.respond(HttpStatusCode.OK, user.toAdminDto())
            }
        }
    }
}

private fun ApplicationCallPipeline.roleCheck(requiredRole: UserType) {
    intercept(ApplicationCallPipeline.Call) {
        val principal = call.principal<JWTPrincipal>()
        val userType =
            principal?.payload?.getClaim("userType")?.asString()?.let { UserType.valueOf(it) }
        if (userType != requiredRole) {
            call.respond(HttpStatusCode.Forbidden, "Insufficient permissions.")
            finish()
        }
    }
}