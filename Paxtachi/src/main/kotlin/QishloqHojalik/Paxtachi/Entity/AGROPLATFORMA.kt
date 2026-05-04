package QishloqHojalik.Paxtachi.Entity


import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.*

@Entity
@Table(name = "farmer_pump_aggregates")
class FarmerPumpAggregate(

    var farmerName: String? = null,
    var inn: String? = null,
    var address: String? = null,

    var pumpModel: String? = null,
    var productionYear: Int? = null,

    var waterLiftHeight: Double? = null,
    var waterFlow: Double? = null,

    var enginePower: String? = null,
    var waterSource: String? = null,

    var connectedArea: Double? = null

) : BaseEntity()


@Entity
@Table(name = "pump_stations")
class PumpStation(

    var stationName: String? = null,

    var installedAggregateCount: Int? = null,
    var workingAggregateCount: Int? = null,

    var waterSource: String? = null

) : BaseEntity()

@Entity
@Table(name = "pump_station_technicals")
class PumpStationTechnical(

    var pumpModel: String? = null,

    @Column(columnDefinition = "TEXT")
    var electricEngineModel: String? = null,

    var waterFlow: Double? = null,
    var enginePowerKw: Double? = null,
    var engineRotation: Double? = null,

    var waterLiftHeight: Double? = null,
    var commissioningYear: Int? = null,

    var attachedArea: Double? = null,
    var balanceValue: Double? = null,

    var pressurePipeDiameter: Double? = null,
    var pressurePipeLength: Double? = null,
    var pressurePipeTotalLength: Double? = null,

    var usedFarmers: String? = null, // text qilib saqlaymiz (keyin normalizatsiya qilsa bo‘ladi)
    var inn: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    var pumpStation: PumpStation? = null

) : BaseEntity()