package team.bupt.h7.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import team.bupt.h7.exceptions.AuthorizationException
import team.bupt.h7.models.PlaceSeekerCreateRequest
import team.bupt.h7.models.PlaceSeekerUpdateRequest
import team.bupt.h7.models.toDto
import team.bupt.h7.services.PlaceSeekerService
import team.bupt.h7.services.UserService
import team.bupt.h7.utils.toPlaceSeekerQueryParams

fun Route.placeSeekerRouting() {
    val userService: UserService by inject()
    val placeSeekerService: PlaceSeekerService by inject()

    route("/placeSeekers") {
        get("/") {
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
            val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 10
            val queryParams = call.request.queryParameters.toPlaceSeekerQueryParams()
            val placeSeekers = placeSeekerService.getPlaceSeekers(page, pageSize, queryParams)
            call.respond(placeSeekers.map { it.toDto() })
        }

        get("/{placeSeekerId}") {
            val placeSeekerId = call.parameters["placeSeekerId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("Invalid place seeker id.")
            val placeSeeker = placeSeekerService.getPlaceSeekerById(placeSeekerId)
                ?: throw IllegalArgumentException("Place seeker not found.")
            call.respond(placeSeeker.toDto())
        }

        authenticate {
            post("/") {
                val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()
                    ?: throw AuthorizationException()
                val user = userService.getUserById(userId) ?: throw AuthorizationException()
                val request = call.receive<PlaceSeekerCreateRequest>()
                val placeSeeker = placeSeekerService.createPlaceSeeker(user, request)
                call.respond(placeSeeker.toDto())
            }

            get("/mine") {
                val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()
                    ?: throw AuthorizationException()
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 10
                val queryParams = call.request.queryParameters.toPlaceSeekerQueryParams()
                val queryParamsWithUserId = queryParams.copy(userId = userId)
                val placeSeekers =
                    placeSeekerService.getPlaceSeekers(page, pageSize, queryParamsWithUserId)
                call.respond(placeSeekers.map { it.toDto() })
            }

            route("/{placeSeekerId}") {
                patch {
                    // TODO: DRY
                    val userId =
                        call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()
                            ?: throw AuthorizationException()
                    val placeSeekerId = call.parameters["placeSeekerId"]?.toLongOrNull()
                        ?: throw IllegalArgumentException("Invalid place seeker id.")
                    val placeSeeker = placeSeekerService.getPlaceSeekerById(placeSeekerId)
                        ?: throw IllegalArgumentException("Place seeker not found.")
                    if (placeSeeker.user.id != userId) {
                        throw AuthorizationException()
                    }
                    val request = call.receive<PlaceSeekerUpdateRequest>()
                    val updatedPlaceSeeker =
                        placeSeekerService.updatePlaceSeeker(placeSeekerId, request)
                            ?: throw IllegalArgumentException("Place seeker not found.")
                    call.respond(updatedPlaceSeeker.toDto())
                }

                delete {
                    val userId =
                        call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()
                            ?: throw AuthorizationException()
                    val placeSeekerId = call.parameters["placeSeekerId"]?.toLongOrNull()
                        ?: throw IllegalArgumentException("Invalid place seeker id.")
                    val placeSeeker = placeSeekerService.getPlaceSeekerById(placeSeekerId)
                        ?: throw IllegalArgumentException("Place seeker not found.")
                    if (placeSeeker.user.id != userId) {
                        throw AuthorizationException()
                    }
                    placeSeekerService.cancelPlaceSeeker(placeSeekerId)
                    call.respond(mapOf("success" to true))
                }
            }
        }
    }
}