package QishloqHojalik.Paxtachi.Repositories


import QishloqHojalik.Paxtachi.Entity.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FarmerPumpAggregateRepository : JpaRepository<FarmerPumpAggregate, Long> {
    fun findAllByDeletedFalse(): List<FarmerPumpAggregate>
    fun findByInnAndDeletedFalse(inn: String): List<FarmerPumpAggregate>
}

@Repository
interface PumpStationRepository : JpaRepository<PumpStation, Long> {
    fun findAllByDeletedFalse(): List<PumpStation>
    fun findByStationNameAndDeletedFalse(stationName: String): PumpStation?
}

@Repository
interface PumpStationTechnicalRepository : JpaRepository<PumpStationTechnical, Long> {
    fun findAllByDeletedFalse(): List<PumpStationTechnical>
    fun findAllByPumpStationIdAndDeletedFalse(stationId: Long): List<PumpStationTechnical>
    fun findAllByInnAndDeletedFalse(inn: String): List<PumpStationTechnical>
}