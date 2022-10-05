package me.maurohahn.crudapi.exception

import me.maurohahn.crudapi.dto.AppMsgDto
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.HttpRequestMethodNotSupportedException

@ControllerAdvice
class GlobalExceptionController {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(Throwable::class)
    fun generalExceptionHandler(throwable: Throwable, req: WebRequest): ResponseEntity<*> {
        logger.error(throwable.message, throwable)

        val path = (req as ServletWebRequest).request.requestURL.toString()

        when {
            throwable is HttpException -> {
                val bodyError = AppMsgDto(
                    code = throwable.status.toString(),
                    message = throwable.message,
                    path = path
                )
                return ResponseEntity(bodyError, throwable.status)

            }
            throwable.cause is HttpException -> {
                val error = throwable.cause as HttpException
                val bodyError = AppMsgDto(
                    code = error.status.toString(),
                    message = error.message,
                    path = path
                )
                return ResponseEntity(bodyError, error.status)
            }
            throwable is AccessDeniedException -> {
                val bodyError = AppMsgDto(
                    code = HttpStatus.UNAUTHORIZED.name,
                    message = "accessDenied",
                    path = path
                )
                return ResponseEntity(bodyError, HttpStatus.UNAUTHORIZED)

            }
            throwable.cause is AccessDeniedException -> {
                val bodyError = AppMsgDto(
                    code = HttpStatus.UNAUTHORIZED.name,
                    message = "accessDenied",
                    path = path
                )
                return ResponseEntity(bodyError, HttpStatus.UNAUTHORIZED)
            }
            throwable is MethodArgumentNotValidException -> {
                val errors = throwable.bindingResult
                    .fieldErrors
                    .map { it.defaultMessage!! }

                val bodyError = AppMsgDto(
                    code = HttpStatus.BAD_REQUEST.name,
                    message = errors.joinToString { ", " },
                    path = path
                )

                return ResponseEntity(bodyError, HttpStatus.BAD_REQUEST)
            }
            throwable is HttpRequestMethodNotSupportedException -> {
                val bodyError = AppMsgDto(
                    code = HttpStatus.METHOD_NOT_ALLOWED.name,
                    message = "methodNotAllowed -> ${throwable.method}",
                    path = path
                )
                return ResponseEntity(bodyError, HttpStatus.METHOD_NOT_ALLOWED)

            }
            else -> {
                val bodyError = AppMsgDto(
                    code = HttpStatus.INTERNAL_SERVER_ERROR.name,
                    message = "unknownError",
                    path = path
                )
                return ResponseEntity(bodyError, HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }

    }

}