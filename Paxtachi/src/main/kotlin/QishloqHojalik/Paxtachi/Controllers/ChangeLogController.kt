package QishloqHojalik.Paxtachi.Controllers

import QishloqHojalik.Paxtachi.Entity.ChangeLog
import QishloqHojalik.Paxtachi.Repositories.ChangeLogRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.*
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/change-logs")
class ChangeLogController(private val repo: ChangeLogRepository) {

    data class ChangeLogResponse(
        val id: Long,
        val tableName: String,
        val recordId: Long,
        val username: String,
        val direction: String,
        val action: String,
        val reason: String,
        val changedAt: String
    )

    private val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    private fun ChangeLog.toResponse() = ChangeLogResponse(
        id = id,
        tableName = tableName,
        recordId = recordId,
        username = username,
        direction = direction ?: "",
        action = action ?: "UPDATE",
        reason = reason,
        changedAt = changedAt.format(fmt)
    )

    @GetMapping("/my")
    fun myLogs(request: HttpServletRequest): List<ChangeLogResponse> {
        val username = request.getAttribute("username") as? String
            ?: throw RuntimeException("Token ichida username yo'q")
        return repo.findAllByUsernameOrderByChangedAtDesc(username).map { it.toResponse() }
    }

    @GetMapping("/all")
    fun allLogs(request: HttpServletRequest): List<ChangeLogResponse> {
        val role      = request.getAttribute("role")      as? String ?: throw RuntimeException("Token ichida role yo'q")
        val direction = request.getAttribute("direction") as? String ?: ""
        if (role != "SUPER_ADMIN" && direction != "HOKIM")
            throw RuntimeException("Faqat super admin yoki hokim ko'ra oladi")
        return repo.findAllByOrderByChangedAtDesc().map { it.toResponse() }
    }
}
