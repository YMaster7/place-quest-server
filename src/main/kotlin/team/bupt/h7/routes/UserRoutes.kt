package team.bupt.h7.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get
import team.bupt.h7.exceptions.InvalidUrlParametersException
import team.bupt.h7.models.entities.UserType
import team.bupt.h7.models.requests.UserCreateRequest
import team.bupt.h7.models.requests.UserLoginRequest
import team.bupt.h7.models.requests.UserUpdateRequest
import team.bupt.h7.models.responses.LoginResponse
import team.bupt.h7.models.responses.toBasicResponse
import team.bupt.h7.models.responses.toSelfResponse
import team.bupt.h7.services.UserService
import team.bupt.h7.utils.getUserIdFromToken
import team.bupt.h7.utils.roleCheck

fun Route.userRouting(userService: UserService) {
    route("/users") {
        post {
            val request = call.receive<UserCreateRequest>()
            val user = userService.createUser(request)
            call.respond(user.toSelfResponse())
        }

        post("/login") {
            val request = call.receive<UserLoginRequest>()
            val user = userService.getUserByUsername(request.username)
            val token = userService.login(user.userId, request.password)
            val response = LoginResponse(
                token = token, userType = user.userType
            )
            call.respond(response)
        }

        authenticate {
            get("/me") {
                val userId = call.getUserIdFromToken()
                val user = userService.getUserById(userId)
                call.respond(user.toSelfResponse())
            }

            patch("/me") {
                val userId = call.getUserIdFromToken()
                val request = call.receive<UserUpdateRequest>()
                val updatedUser = userService.updateUser(userId, request)
                call.respond(updatedUser.toSelfResponse())
            }

            get("/{id}") {
                val userId = call.parameters["id"]?.toLong()
                    ?: throw InvalidUrlParametersException()
                val user = userService.getUserById(userId)
                call.respond(user.toBasicResponse())
            }
        }
    }

    route("/admin") {
        authenticate {
            roleCheck(UserType.Admin)

            userAdminRouting(get())
        }
    }
}
