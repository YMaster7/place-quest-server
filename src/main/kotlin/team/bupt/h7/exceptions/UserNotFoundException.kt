package team.bupt.h7.exceptions

import io.ktor.http.*

class UserNotFoundException : ApplicationException(
    httpStatusCode = HttpStatusCode.NotFound,
    internalErrorCode = ErrorCode.UserNotFound,
)
