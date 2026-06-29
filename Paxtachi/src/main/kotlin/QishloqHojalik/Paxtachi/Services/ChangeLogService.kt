package QishloqHojalik.Paxtachi.Services

import QishloqHojalik.Paxtachi.Entity.ChangeLog
import QishloqHojalik.Paxtachi.Repositories.ChangeLogRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service

@Service
class ChangeLogService(private val repo: ChangeLogRepository) {
    fun log(
        request: HttpServletRequest,
        tableName: String,
        recordId: Long,
        reason: String,
        action: String = "UPDATE"
    ) {
        val username = request.getAttribute("username") as? String ?: "unknown"
        val direction = request.getAttribute("direction") as? String ?: ""
        repo.save(
            ChangeLog(
                tableName = tableName,
                recordId = recordId,
                username = username,
                direction = direction,
                action = action,
                reason = reason
            )
        )
    }
}
