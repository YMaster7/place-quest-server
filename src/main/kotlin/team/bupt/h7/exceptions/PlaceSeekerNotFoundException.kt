package team.bupt.h7.exceptions

import io.ktor.http.*

class PlaceSeekerNotFoundException : ApplicationException(
    httpStatusCode = HttpStatusCode.NotFound,
    internalErrorCode = ErrorCode.PlaceSeekerNotFound,
)
