package team.bupt.h7.exceptions

import io.ktor.http.*

class UserAlreadyExistsException : ApplicationException(
    httpStatusCode = HttpStatusCode.Conflict,
    internalErrorCode = ErrorCode.UserExists,
)
