package team.bupt.h7.exceptions

import io.ktor.http.*
import team.bupt.h7.models.responses.ErrorResponse

open class ApplicationException(
    val httpStatusCode: HttpStatusCode,
    private val internalErrorCode: ErrorCode,
    private val externalErrorCode: ErrorCode = internalErrorCode,
) : Exception(internalErrorCode.errorMessage) {
    open fun toErrorResponse(): ErrorResponse {
        return ErrorResponse(
            errorCode = externalErrorCode.errorCode,
            errorMessage = externalErrorCode.errorMessage
        )
    }
}
