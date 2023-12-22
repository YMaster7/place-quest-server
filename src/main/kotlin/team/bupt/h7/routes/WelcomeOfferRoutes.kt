package team.bupt.h7.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import team.bupt.h7.exceptions.AuthorizationException
import team.bupt.h7.models.WelcomeOfferCreateRequest
import team.bupt.h7.models.WelcomeOfferUpdateRequest
import team.bupt.h7.models.toDto
import team.bupt.h7.services.WelcomeOfferService
import team.bupt.h7.utils.toWelcomeOfferQueryParams

fun Route.welcomeOfferRouting() {
    val welcomeOfferService: WelcomeOfferService by inject()

    route("/welcomeOffers") {
        get("/") {
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
            val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 10
            val queryParams = call.request.queryParameters.toWelcomeOfferQueryParams()
            val welcomeOffers = welcomeOfferService.getWelcomeOffers(page, pageSize, queryParams)
            call.respond(welcomeOffers.map { it.toDto() })
        }

        get("/{welcomeOfferId}") {
            val welcomeOfferId = call.parameters["welcomeOfferId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("Invalid welcome offer id.")
            val welcomeOffer = welcomeOfferService.getWelcomeOfferById(welcomeOfferId)
                ?: throw IllegalArgumentException("Welcome offer not found.")
            call.respond(welcomeOffer.toDto())
        }

        authenticate {
            post("/") {
                val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()
                    ?: throw AuthorizationException()
                val request = call.receive<WelcomeOfferCreateRequest>()
                val welcomeOffer = welcomeOfferService.createWelcomeOffer(userId, request)
                call.respond(welcomeOffer.toDto())
            }

            get("/mine") {
                val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()
                    ?: throw AuthorizationException()
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 10
                val queryParams = call.request.queryParameters.toWelcomeOfferQueryParams()
                val queryParamsWithUserId = queryParams.copy(userId = userId)
                val welcomeOffers =
                    welcomeOfferService.getWelcomeOffers(page, pageSize, queryParamsWithUserId)
                call.respond(welcomeOffers.map { it.toDto() })
            }

            route("/{welcomeOfferId}") {
                patch {
                    val userId =
                        call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()
                            ?: throw AuthorizationException()
                    val welcomeOfferId = call.parameters["welcomeOfferId"]?.toLongOrNull()
                        ?: throw IllegalArgumentException("Invalid welcome offer id.")
                    val welcomeOffer = welcomeOfferService.getWelcomeOfferById(welcomeOfferId)
                        ?: throw IllegalArgumentException("Welcome offer not found.")
                    if (welcomeOffer.user.id != userId) {
                        throw AuthorizationException()
                    }
                    val request = call.receive<WelcomeOfferUpdateRequest>()
                    val updatedWelcomeOffer =
                        welcomeOfferService.updateWelcomeOffer(welcomeOfferId, request)
                            ?: throw IllegalArgumentException("Welcome offer not found.")
                    call.respond(updatedWelcomeOffer.toDto())
                }

                delete {
                    val userId =
                        call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()
                            ?: throw AuthorizationException()
                    val welcomeOfferId = call.parameters["welcomeOfferId"]?.toLongOrNull()
                        ?: throw IllegalArgumentException("Invalid welcome offer id.")
                    val welcomeOffer = welcomeOfferService.getWelcomeOfferById(welcomeOfferId)
                        ?: throw IllegalArgumentException("Welcome offer not found.")
                    if (welcomeOffer.user.id != userId) {
                        throw AuthorizationException()
                    }
                    val success = welcomeOfferService.cancelWelcomeOffer(welcomeOfferId)
                    if (!success) {
                        throw IllegalArgumentException("Welcome offer not found.")
                    }
                    call.respond(mapOf("success" to true))
                }
            }
        }
    }
}