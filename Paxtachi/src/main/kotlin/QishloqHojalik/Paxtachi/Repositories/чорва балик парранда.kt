package QishloqHojalik.Paxtachi.Repositories

import QishloqHojalik.Paxtachi.Entity.FarmRepresentative
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FarmRepresentativeRepository : JpaRepository<FarmRepresentative, Long> {

    fun findAllByDeletedFalse(): List<FarmRepresentative>

    fun findByIdAndDeletedFalse(id: Long): FarmRepresentative?

    fun findAllByInnAndDeletedFalse(inn: String): List<FarmRepresentative>

    fun findAllByFarmNameContainingIgnoreCaseAndDeletedFalse(farmName: String): List<FarmRepresentative>
}