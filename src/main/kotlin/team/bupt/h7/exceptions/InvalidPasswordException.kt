package team.bupt.h7.exceptions

import io.ktor.http.*

class InvalidPasswordException : ApplicationException(
    httpStatusCode = HttpStatusCode.Unauthorized,
    internalErrorCode = ErrorCode.InvalidPassword,
    externalErrorCode = ErrorCode.UsernameOrPasswordInvalid,
)
