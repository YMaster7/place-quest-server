package team.bupt.h7.exceptions

import io.ktor.http.*

class InsufficientPermissionsException : ApplicationException(
    httpStatusCode = HttpStatusCode.Unauthorized,
    internalErrorCode = ErrorCode.InsufficientPermissions,
)
