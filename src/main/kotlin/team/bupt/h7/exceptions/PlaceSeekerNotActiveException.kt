package team.bupt.h7.exceptions

import io.ktor.http.*

class PlaceSeekerNotActiveException : ApplicationException(
    httpStatusCode = HttpStatusCode.BadRequest,
    internalErrorCode = ErrorCode.PlaceSeekerNotActive,
)