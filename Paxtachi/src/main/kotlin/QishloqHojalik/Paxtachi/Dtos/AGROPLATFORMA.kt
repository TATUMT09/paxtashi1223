package QishloqHojalik.Paxtachi.Dtos

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import QishloqHojalik.Paxtachi.Entity.*
data class FarmerPumpAggregateDto(
    val farmerName: String?,
    val inn: String?,
    val address: String?,
    val pumpModel: String?,
    val productionYear: Int?,
    val waterLiftHeight: Double?,
    val waterFlow: Double?,
    val enginePower: String?,
    val waterSource: String?,
    val connectedArea: Double?
)

data class FarmerPumpAggregateResponseDto(
    val id: Long,
    val farmerName: String?,
    val inn: String?,
    val address: String?,
    val pumpModel: String?,
    val productionYear: Int?,
    val waterLiftHeight: Double?,
    val waterFlow: Double?,
    val enginePower: String?,
    val waterSource: String?,
    val connectedArea: Double?
)

data class PumpStationDto(
    val stationName: String?,
    val installedAggregateCount: Int?,
    val workingAggregateCount: Int?,
    val waterSource: String?
)

data class PumpStationResponseDto(
    val id: Long,
    val stationName: String?,
    val installedAggregateCount: Int?,
    val workingAggregateCount: Int?,
    val waterSource: String?
)

data class PumpStationTechnicalDto(
    val stationId: Long?,
    val pumpModel: String?,
    val electricEngineModel: String?,
    val waterFlow: Double?,
    val enginePowerKw: Double?,
    val engineRotation: Double?,
    val waterLiftHeight: Double?,
    val commissioningYear: Int?,
    val attachedArea: Double?,
    val balanceValue: Double?,
    val pressurePipeDiameter: Double?,
    val pressurePipeLength: Double?,
    val pressurePipeTotalLength: Double?,
    val usedFarmers: String?,
    val inn: String?
)

data class PumpStationTechnicalResponseDto(
    val id: Long,
    val stationId: Long?,
    val stationName: String?,
    val pumpModel: String?,
    val electricEngineModel: String?,
    val waterFlow: Double?,
    val enginePowerKw: Double?,
    val engineRotation: Double?,
    val waterLiftHeight: Double?,
    val commissioningYear: Int?,
    val attachedArea: Double?,
    val balanceValue: Double?,
    val pressurePipeDiameter: Double?,
    val pressurePipeLength: Double?,
    val pressurePipeTotalLength: Double?,
    val usedFarmers: String?,
    val inn: String?
)




fun FarmerPumpAggregate.toResponseDto() = FarmerPumpAggregateResponseDto(
    id = this.id,
    farmerName = this.farmerName,
    inn = this.inn,
    address = this.address,
    pumpModel = this.pumpModel,
    productionYear = this.productionYear,
    waterLiftHeight = this.waterLiftHeight,
    waterFlow = this.waterFlow,
    enginePower = this.enginePower,
    waterSource = this.waterSource,
    connectedArea = this.connectedArea
)

fun PumpStation.toResponseDto() = PumpStationResponseDto(
    id = this.id,
    stationName = this.stationName,
    installedAggregateCount = this.installedAggregateCount,
    workingAggregateCount = this.workingAggregateCount,
    waterSource = this.waterSource
)

fun PumpStationTechnical.toResponseDto() = PumpStationTechnicalResponseDto(
    id = this.id,
    stationId = this.pumpStation?.id,
    stationName = this.pumpStation?.stationName,
    pumpModel = this.pumpModel,
    electricEngineModel = this.electricEngineModel,
    waterFlow = this.waterFlow,
    enginePowerKw = this.enginePowerKw,
    engineRotation = this.engineRotation,
    waterLiftHeight = this.waterLiftHeight,
    commissioningYear = this.commissioningYear,
    attachedArea = this.attachedArea,
    balanceValue = this.balanceValue,
    pressurePipeDiameter = this.pressurePipeDiameter,
    pressurePipeLength = this.pressurePipeLength,
    pressurePipeTotalLength = this.pressurePipeTotalLength,
    usedFarmers = this.usedFarmers,
    inn = this.inn
)



fun Cell?.text(): String? {
    if (this == null) return null

    return try {
        when (cellType) {
            CellType.STRING -> stringCellValue.trim()
            CellType.NUMERIC -> {
                val num = numericCellValue
                if (num % 1.0 == 0.0) num.toLong().toString() else num.toString()
            }
            CellType.BOOLEAN -> booleanCellValue.toString()
            CellType.FORMULA -> {
                try {
                    stringCellValue.trim()
                } catch (e: Exception) {
                    numericCellValue.toString()
                }
            }
            else -> null
        }
    } catch (e: Exception) {
        null
    }
}

fun Cell?.doubleValue(): Double? {
    val value = this.text() ?: return null
    return value.replace(",", ".").trim().toDoubleOrNull()
}

fun Cell?.intValue(): Int? {
    return this.doubleValue()?.toInt()
}