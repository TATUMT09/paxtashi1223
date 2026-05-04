package QishloqHojalik.Paxtachi.Repositories

import QishloqHojalik.Paxtachi.Entity.AgroTechnic
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AgroTechnicRepository : JpaRepository<AgroTechnic, Long> {

    fun findAllByDeletedFalse(): List<AgroTechnic>

    fun findByIdAndDeletedFalse(id: Long): AgroTechnic?

    fun findAllByInnAndDeletedFalse(inn: String): List<AgroTechnic>
}