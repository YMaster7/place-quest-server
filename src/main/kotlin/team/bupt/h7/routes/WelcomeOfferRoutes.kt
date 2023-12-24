package team.bupt.h7.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import team.bupt.h7.exceptions.InvalidUrlParametersException
import team.bupt.h7.models.requests.WelcomeOfferCreateRequest
import team.bupt.h7.models.requests.WelcomeOfferUpdateRequest
import team.bupt.h7.models.responses.toResponse
import team.bupt.h7.services.WelcomeOfferService
import team.bupt.h7.utils.getUserIdFromToken
import team.bupt.h7.utils.toWelcomeOfferQueryParams

fun Route.welcomeOfferRouting(welcomeOfferService: WelcomeOfferService) {
    route("/offers") {
        get {
            val page = call.request.queryParameters["page"]?.toInt() ?: 1
            val pageSize = call.request.queryParameters["page_size"]?.toInt() ?: 10
            val params = call.request.queryParameters.toWelcomeOfferQueryParams()
            val welcomeOffers = welcomeOfferService.queryWelcomeOffers(page, pageSize, params)
            call.respond(welcomeOffers.map { it.toResponse() })
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toLong()
                ?: throw InvalidUrlParametersException()
            val welcomeOffer = welcomeOfferService.getWelcomeOfferById(id)
            call.respond(welcomeOffer.toResponse())
        }

        authenticate {
            post {
                val userId = call.getUserIdFromToken()
                val request = call.receive<WelcomeOfferCreateRequest>()
                val welcomeOffer = welcomeOfferService.createWelcomeOffer(userId, request)
                call.respond(welcomeOffer.toResponse())
            }

            get("/mine") {
                val userId = call.getUserIdFromToken()
                val page = call.request.queryParameters["page"]?.toInt() ?: 1
                val pageSize = call.request.queryParameters["page_size"]?.toInt() ?: 10
                val params = call.request.queryParameters.toWelcomeOfferQueryParams()

                val paramsWithUserId = params.copy(userId = userId)
                val welcomeOffers =
                    welcomeOfferService.queryWelcomeOffers(page, pageSize, paramsWithUserId)
                call.respond(welcomeOffers.map { it.toResponse() })
            }

            patch("/{id}") {
                val userId = call.getUserIdFromToken()
                val id = call.parameters["id"]?.toLong()
                    ?: throw InvalidUrlParametersException()
                val request = call.receive<WelcomeOfferUpdateRequest>()
                val updatedWelcomeOffer =
                    welcomeOfferService.updateWelcomeOffer(userId, id, request)
                call.respond(updatedWelcomeOffer.toResponse())
            }

            delete("/{id}") {
                val userId = call.getUserIdFromToken()
                val id = call.parameters["id"]?.toLong()
                    ?: throw InvalidUrlParametersException()
                val welcomeOffer = welcomeOfferService.cancelWelcomeOffer(userId, id)
                call.respond(welcomeOffer.toResponse())
            }
        }
    }
}