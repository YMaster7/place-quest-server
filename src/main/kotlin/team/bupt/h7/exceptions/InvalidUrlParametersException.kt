package team.bupt.h7.exceptions

import io.ktor.http.*

class InvalidUrlParametersException : ApplicationException(
    httpStatusCode = HttpStatusCode.BadRequest,
    internalErrorCode = ErrorCode.InvalidUrlParameters,
)
