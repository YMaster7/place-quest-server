package team.bupt.h7.exceptions

import io.ktor.http.*

class WelcomeOfferNotActiveException : ApplicationException(
    httpStatusCode = HttpStatusCode.BadRequest,
    internalErrorCode = ErrorCode.WelcomeOfferNotActive,
)