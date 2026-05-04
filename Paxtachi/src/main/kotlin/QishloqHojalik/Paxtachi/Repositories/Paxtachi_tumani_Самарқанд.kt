package QishloqHojalik.Paxtachi.Repositories

import QishloqHojalik.Paxtachi.Entity.CadastreLandRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface CadastreLandRecordRepository : JpaRepository<CadastreLandRecord, Long> {

    // 🔹 faqat o‘chirilmaganlar
    fun findAllByDeletedFalse(): List<CadastreLandRecord>

    // 🔹 ID bo‘yicha (soft delete bilan)
    fun findByIdAndDeletedFalse(id: Long): CadastreLandRecord?


}