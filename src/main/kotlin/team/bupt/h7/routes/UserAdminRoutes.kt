package team.bupt.h7.routes

import io.ktor.server.routing.*
import team.bupt.h7.services.UserService

fun Route.userAdminRouting(userService: UserService) {
    route("/users") {
        // TODO: add admin operations
    }
}
