package  org.amfibot.discord.api.exceptions.crud

import org.amfibot.discord.api.exceptions.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class CRUDExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(ResourceNotFoundException::class)
    fun resourceNotFoundHandler(): ResponseEntity<ExceptionResponse> {
        return ResponseEntity
            .status(404)
            .body(
                ExceptionResponse(
                    status = HttpStatus.valueOf(404),
                    message = "Resource not found."
                )
            )
    }
    @ExceptionHandler(ResourceAlreadyExistsException::class)
    fun resourceAlreadyExistsHandler(): ResponseEntity<ExceptionResponse> {
        return ResponseEntity
            .status(400)
            .body(
                ExceptionResponse(
                    status = HttpStatus.valueOf(400),
                    message = "Bad request",
                    error = "The resource already exists."
                )
            )
    }
}