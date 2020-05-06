package pl.dudzinski.clinic.exception;

import org.apache.commons.lang.exception.ExceptionUtils
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException

@ControllerAdvice
class ControllerExceptionHandler {

    private val logger = LoggerFactory.getLogger(ControllerExceptionHandler::class.java)

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(exception: ResponseStatusException): ResponseEntity<ErrorMessage?>? {
        logger.error("ExceptionHandler handled exception: " + exception.message, exception)
        return ResponseEntity(ErrorMessage.fromResponseStatusException(exception), exception.status)
    }

    @ExceptionHandler(Throwable::class)
    fun handleThrowable(throwable: Throwable): ResponseEntity<ErrorMessage?>? {
        logger.error("ExceptionHandler handled throwable: " + throwable.message + ", root cause message: "
                + ExceptionUtils.getRootCauseMessage(throwable), throwable)
        return ResponseEntity(fromThrowable(throwable), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    private fun fromThrowable(throwable: Throwable): ErrorMessage =
            ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), throwable.message)

}
