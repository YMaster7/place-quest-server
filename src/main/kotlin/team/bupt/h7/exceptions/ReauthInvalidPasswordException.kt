package team.bupt.h7.exceptions

import io.ktor.http.*

class ReauthInvalidPasswordException : ApplicationException(
    httpStatusCode = HttpStatusCode.Unauthorized,
    internalErrorCode = ErrorCode.InvalidPassword,
)