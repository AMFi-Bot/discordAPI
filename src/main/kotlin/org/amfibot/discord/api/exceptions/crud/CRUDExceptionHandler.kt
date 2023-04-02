package  org.amfibot.discord.api.exceptions.crud

import org.amfibot.discord.api.exceptions.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * A handler for CRUD-based exceptions
 */
@RestControllerAdvice
class CRUDExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(ResourceNotFoundException::class)
    fun resourceNotFoundHandler(ex: ResourceNotFoundException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity
            .status(404)
            .body(
                ExceptionResponse(
                    status = HttpStatus.valueOf(404),
                    message = ex.message ?: "Resource not found."
                )
            )
    }
    @ExceptionHandler(ResourceAlreadyExistsException::class)
    fun resourceAlreadyExistsHandler(ex: ResourceAlreadyExistsException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity
            .status(400)
            .body(
                ExceptionResponse(
                    status = HttpStatus.valueOf(400),
                    message = "Bad request",
                    error = ex.message ?: "The resource already exists."
                )
            )
    }
}