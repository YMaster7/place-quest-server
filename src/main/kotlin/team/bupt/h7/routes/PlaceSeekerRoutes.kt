package team.bupt.h7.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import team.bupt.h7.exceptions.InvalidUrlParametersException
import team.bupt.h7.models.requests.PlaceSeekerCreateRequest
import team.bupt.h7.models.requests.PlaceSeekerUpdateRequest
import team.bupt.h7.models.responses.toResponse
import team.bupt.h7.services.PlaceSeekerService
import team.bupt.h7.utils.PageStatWrapper
import team.bupt.h7.utils.getUserIdFromToken
import team.bupt.h7.utils.toPlaceSeekerQueryParams

fun Route.placeSeekerRouting(placeSeekerService: PlaceSeekerService) {
    route("/seekers") {
        get {
            val page = call.request.queryParameters["page"]?.toInt() ?: 1
            val pageSize = call.request.queryParameters["page_size"]?.toInt() ?: 10
            val params = call.request.queryParameters.toPlaceSeekerQueryParams()
            val (placeSeekers, pageNum) = placeSeekerService.queryPlaceSeekers(
                page,
                pageSize,
                params
            )
            call.respond(
                PageStatWrapper(
                    page,
                    pageSize,
                    pageNum,
                    placeSeekers.map { it.toResponse() }
            ))
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toLong()
                ?: throw InvalidUrlParametersException()
            val placeSeeker = placeSeekerService.getPlaceSeekerById(id)
            call.respond(placeSeeker.toResponse())
        }

        authenticate {
            post {
                val userId = call.getUserIdFromToken()
                val request = call.receive<PlaceSeekerCreateRequest>()
                val placeSeeker = placeSeekerService.createPlaceSeeker(userId, request)
                call.respond(placeSeeker.toResponse())
            }

            get("/mine") {
                val userId = call.getUserIdFromToken()
                val page = call.request.queryParameters["page"]?.toInt() ?: 1
                val pageSize = call.request.queryParameters["page_size"]?.toInt() ?: 10
                val params = call.request.queryParameters.toPlaceSeekerQueryParams()

                val paramsWithUserId = params.copy(userId = userId)
                val (placeSeekers, pageNum) =
                    placeSeekerService.queryPlaceSeekers(page, pageSize, paramsWithUserId)
                call.respond(
                    PageStatWrapper(
                        page,
                        pageSize,
                        pageNum,
                        placeSeekers.map { it.toResponse() }
                ))
            }

            patch("/{id}") {
                val userId = call.getUserIdFromToken()
                val id = call.parameters["id"]?.toLong()
                    ?: throw InvalidUrlParametersException()
                val request = call.receive<PlaceSeekerUpdateRequest>()
                val updatedPlaceSeeker = placeSeekerService.updatePlaceSeeker(userId, id, request)
                call.respond(updatedPlaceSeeker.toResponse())
            }

            delete("/{id}") {
                val userId = call.getUserIdFromToken()
                val id = call.parameters["id"]?.toLong()
                    ?: throw InvalidUrlParametersException()
                val placeSeeker = placeSeekerService.cancelPlaceSeeker(userId, id)
                call.respond(placeSeeker.toResponse())
            }
        }
    }
}