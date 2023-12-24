package team.bupt.h7.exceptions

import io.ktor.http.*

class WelcomeOfferNotFoundException : ApplicationException(
    httpStatusCode = HttpStatusCode.NotFound,
    internalErrorCode = ErrorCode.WelcomeOfferNotFound,
)
