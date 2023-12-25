package team.bupt.h7.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import team.bupt.h7.models.entities.UserType
import team.bupt.h7.models.responses.toResponse
import team.bupt.h7.services.SeekPlaceDealService
import team.bupt.h7.utils.roleCheck
import team.bupt.h7.utils.toSeekPlaceDealStatisticQueryParams

fun Route.seekPlaceDealRouting(seekPlaceDealService: SeekPlaceDealService) {
    route("/deals") {
        authenticate {
            roleCheck(UserType.Admin)

            get("/statistics") {
                val params = call.request.queryParameters.toSeekPlaceDealStatisticQueryParams()
                val stats = seekPlaceDealService.querySeekPlaceDealStatistics(params)
                call.respond(stats.map { it.toResponse() })
            }
        }
    }
}