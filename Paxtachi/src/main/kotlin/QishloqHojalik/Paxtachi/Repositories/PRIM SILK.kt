package QishloqHojalik.Paxtachi.Repositories


import QishloqHojalik.Paxtachi.Entity.GreenhouseRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GreenhouseRecordRepository : JpaRepository<GreenhouseRecord, Long> {

    fun findAllByDeletedFalse(): List<GreenhouseRecord>

    fun findByIdAndDeletedFalse(id: Long): GreenhouseRecord?

    fun findAllByJshshirAndDeletedFalse(jshshir: String): List<GreenhouseRecord>

    fun findAllByOwnerNameContainingIgnoreCaseAndDeletedFalse(ownerName: String): List<GreenhouseRecord>
}