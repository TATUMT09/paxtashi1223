package QishloqHojalik.Paxtachi.Security

import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    // 400 - Validation
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException)
            : ResponseEntity<Map<String, Any>> {

        val errors = ex.bindingResult.fieldErrors.map {
            "${it.field}: ${it.defaultMessage}"
        }

        log.warn("Validation error: {}", errors)

        return buildResponse(HttpStatus.BAD_REQUEST,
            "Noto'g'ri so'rov",
            "VALIDATION_ERROR",
            errors)
    }

    // 401
    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentials(ex: BadCredentialsException)
            : ResponseEntity<Map<String, Any>> {

        log.warn("Unauthorized: {}", ex.message)

        return buildResponse(HttpStatus.UNAUTHORIZED,
            "Login yoki parol noto'g'ri",
            "UNAUTHORIZED",
            emptyList())
    }

    // 403
    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(ex: AccessDeniedException)
            : ResponseEntity<Map<String, Any>> {

        log.warn("Access denied: {}", ex.message)

        return buildResponse(HttpStatus.FORBIDDEN,
            "Ruxsat yo'q",
            "FORBIDDEN",
            emptyList())
    }

    // 404
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(ex: NoSuchElementException)
            : ResponseEntity<Map<String, Any>> {

        log.warn("Not found: {}", ex.message)

        return buildResponse(HttpStatus.NOT_FOUND,
            ex.message ?: "Resurs topilmadi",
            "NOT_FOUND",
            emptyList())
    }

    // Database error
    @ExceptionHandler(DataAccessException::class)
    fun handleDatabase(ex: DataAccessException)
            : ResponseEntity<Map<String, Any>> {

        log.error("Database error", ex)

        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
            "Ma'lumotlar bazasi xatosi",
            "DATABASE_ERROR",
            listOf(ex.rootCause?.message ?: "DB error"))
    }

    // ResponseStatusException
    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatus(ex: ResponseStatusException)
            : ResponseEntity<Map<String, Any>> {

        log.warn("ResponseStatusException: {}", ex.reason)

        return buildResponse(
            HttpStatus.valueOf(ex.statusCode.value()),
            ex.reason ?: "Xato",
            ex.statusCode.toString(),
            emptyList()
        )
    }

    // 🔥 REAL 500
    @ExceptionHandler(Exception::class)
    fun handleGeneral(ex: Exception)
            : ResponseEntity<Map<String, Any>> {

        log.error("Unhandled exception occurred", ex)   // 👈 QIZIL BO‘LIB LOGDA CHIQADI

        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
            "Server xatoligi",
            "INTERNAL_ERROR",
            listOf(ex.localizedMessage ?: "Noma'lum xato"))
    }

    private fun buildResponse(
        status: HttpStatus,
        message: String,
        code: String,
        details: List<Any>
    ): ResponseEntity<Map<String, Any>> {

        return ResponseEntity.status(status).body(
            mapOf(
                "timestamp" to Date(),
                "status" to status.value(),
                "message" to message,
                "code" to code,
                "details" to details
            )
        )
    }
}