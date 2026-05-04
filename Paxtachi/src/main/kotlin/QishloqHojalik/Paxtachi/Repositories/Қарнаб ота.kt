package QishloqHojalik.Paxtachi.Repositories


import QishloqHojalik.Paxtachi.Entity.LandContour
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LandContourRepository : JpaRepository<LandContour, Long> {

    fun findAllByDeletedFalse(): List<LandContour>

    fun findByIdAndDeletedFalse(id: Long): LandContour?

    fun findByContourNumberAndDeletedFalse(contourNumber: String): LandContour?
}

