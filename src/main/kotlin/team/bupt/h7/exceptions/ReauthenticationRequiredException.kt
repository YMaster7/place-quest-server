package team.bupt.h7.exceptions

import io.ktor.http.*

class ReauthenticationRequiredException : ApplicationException(
    httpStatusCode = HttpStatusCode.Unauthorized,
    internalErrorCode = ErrorCode.ReauthenticationRequired,
)
