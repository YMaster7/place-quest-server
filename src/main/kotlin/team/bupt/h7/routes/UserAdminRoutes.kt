package team.bupt.h7.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import team.bupt.h7.exceptions.InvalidUrlParametersException
import team.bupt.h7.models.responses.toAdminResponse
import team.bupt.h7.services.UserService

fun Route.userAdminRouting(userService: UserService) {
    route("/users") {
        get {
            val page = call.parameters["page"]?.toInt() ?: 1
            val pageSize = call.parameters["page_size"]?.toInt() ?: 10
            val users = userService.queryUsers(page, pageSize)
            call.respond(users.map { it.toAdminResponse() })
        }

        get("/{id}") {
            val userId = call.parameters["id"]?.toLong()
                ?: throw InvalidUrlParametersException()
            val user = userService.getUserById(userId)
            call.respond(user.toAdminResponse())
        }
    }
}
