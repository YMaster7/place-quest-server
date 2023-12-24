package team.bupt.h7.exceptions

import io.ktor.http.*

class UserNotOwnerException : ApplicationException(
    httpStatusCode = HttpStatusCode.Forbidden,
    internalErrorCode = ErrorCode.UserNotOwner,
)
