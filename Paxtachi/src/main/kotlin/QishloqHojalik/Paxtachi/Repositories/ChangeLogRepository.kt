package QishloqHojalik.Paxtachi.Repositories

import QishloqHojalik.Paxtachi.Entity.ChangeLog
import org.springframework.data.jpa.repository.JpaRepository

interface ChangeLogRepository : JpaRepository<ChangeLog, Long> {
    fun findAllByTableNameAndRecordIdOrderByChangedAtDesc(tableName: String, recordId: Long): List<ChangeLog>
    fun findAllByUsernameOrderByChangedAtDesc(username: String): List<ChangeLog>
    fun findAllByOrderByChangedAtDesc(): List<ChangeLog>
    fun findAllByDirectionOrderByChangedAtDesc(direction: String): List<ChangeLog>
}
