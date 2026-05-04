package QishloqHojalik.Paxtachi.Services


import QishloqHojalik.Paxtachi.Dtos.*
import org.springframework.web.multipart.MultipartFile
import QishloqHojalik.Paxtachi.Entity.*
import QishloqHojalik.Paxtachi.Repositories.*
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface PumpExcelService {

    fun importExcel(file: MultipartFile): ExcelImportResponse
    fun getAllFarmers(): List<FarmerPumpAggregateResponseDto>
    fun getFarmerById(id: Long): FarmerPumpAggregateResponseDto
    fun updateFarmer(id: Long, dto: FarmerPumpAggregateDto): FarmerPumpAggregateResponseDto
    fun deleteFarmer(id: Long)
    fun getAllStations(): List<PumpStationResponseDto>
    fun getStationById(id: Long): PumpStationResponseDto
    fun updateStation(id: Long, dto: PumpStationDto): PumpStationResponseDto
    fun deleteStation(id: Long)
    fun getAllTechnicals(): List<PumpStationTechnicalResponseDto>
    fun getTechnicalById(id: Long): PumpStationTechnicalResponseDto
    fun updateTechnical(id: Long, dto: PumpStationTechnicalDto): PumpStationTechnicalResponseDto
    fun deleteTechnical(id: Long)
}


@Service
class PumpExcelServiceImpl(
    private val farmerRepo: FarmerPumpAggregateRepository,
    private val stationRepo: PumpStationRepository,
    private val technicalRepo: PumpStationTechnicalRepository
) : PumpExcelService {

    override fun importExcel(file: MultipartFile): ExcelImportResponse {
        val workbook = XSSFWorkbook(file.inputStream)
        val errors = mutableListOf<String>()

        var farmerSaved = 0
        var stationSaved = 0
        var technicalSaved = 0

        try {
            if (workbook.numberOfSheets > 0) {
                farmerSaved = importFarmerSheet(workbook.getSheetAt(0), errors)
            }

            if (workbook.numberOfSheets > 1) {
                val result = importStationSheet(workbook.getSheetAt(1), errors)
                stationSaved += result.first
                technicalSaved += result.second
            }

            if (workbook.numberOfSheets > 2) {
                val result = importStationSheet(workbook.getSheetAt(2), errors)
                stationSaved += result.first
                technicalSaved += result.second
            }
        } finally {
            workbook.close()
        }

        return ExcelImportResponse(
            farmerPumpAggregateSaved = farmerSaved,
            pumpStationSaved = stationSaved,
            pumpStationTechnicalSaved = technicalSaved,
            errors = errors
        )
    }

    private fun importFarmerSheet(sheet: Sheet, errors: MutableList<String>): Int {
        var saved = 0

        for (i in 5..sheet.lastRowNum) {
            val row = sheet.getRow(i) ?: continue

            val farmerName = row.getCell(1).text()
            if (farmerName.isNullOrBlank()) continue

            try {
                val entity = FarmerPumpAggregate(
                    farmerName = farmerName,
                    inn = row.getCell(2).text(),
                    address = row.getCell(3).text(),
                    pumpModel = row.getCell(4).text(),
                    productionYear = row.getCell(5).intValue(),
                    waterLiftHeight = row.getCell(6).doubleValue(),
                    waterFlow = row.getCell(7).doubleValue(),
                    enginePower = row.getCell(8).text(),
                    waterSource = row.getCell(9).text(),
                    connectedArea = row.getCell(10).doubleValue()
                )

                farmerRepo.save(entity)
                saved++
            } catch (e: Exception) {
                errors.add("Farmer sheet row ${i + 1}: ${e.message}")
            }
        }

        return saved
    }

    private fun importStationSheet(sheet: Sheet, errors: MutableList<String>): Pair<Int, Int> {
        var stationSaved = 0
        var technicalSaved = 0

        var currentStation: PumpStation? = null

        for (i in 5..sheet.lastRowNum) {
            val row = sheet.getRow(i) ?: continue

            val stationName = row.getCell(1).text()

            try {
                if (!stationName.isNullOrBlank()) {
                    currentStation = stationRepo.findByStationNameAndDeletedFalse(stationName)

                    if (currentStation == null) {
                        currentStation = PumpStation(
                            stationName = stationName,
                            installedAggregateCount = row.getCell(2).intValue(),
                            workingAggregateCount = row.getCell(5).intValue(),
                            waterSource = row.getCell(16).text()
                        )

                        currentStation = stationRepo.save(currentStation)
                        stationSaved++
                    }
                }

                if (currentStation == null) continue

                val pumpModel = row.getCell(3).text()
                val engineModel = row.getCell(4).text()

                if (pumpModel.isNullOrBlank() && engineModel.isNullOrBlank()) continue

                val technical = PumpStationTechnical(
                    pumpStation = currentStation,
                    pumpModel = pumpModel,
                    electricEngineModel = engineModel,
                    waterFlow = row.getCell(6).doubleValue(),
                    enginePowerKw = row.getCell(7).doubleValue(),
                    engineRotation = row.getCell(8).doubleValue(),
                    waterLiftHeight = row.getCell(9).doubleValue(),
                    commissioningYear = row.getCell(10).intValue(),
                    attachedArea = row.getCell(11).doubleValue(),
                    balanceValue = row.getCell(12).doubleValue(),
                    pressurePipeDiameter = row.getCell(13).doubleValue(),
                    pressurePipeLength = row.getCell(14).doubleValue(),
                    pressurePipeTotalLength = row.getCell(15).doubleValue(),
                    usedFarmers = row.getCell(17).text(),
                    inn = row.getCell(18).text()
                )

                technicalRepo.save(technical)
                technicalSaved++

            } catch (e: Exception) {
                errors.add("Station sheet ${sheet.sheetName}, row ${i + 1}: ${e.message}")
            }
        }

        return Pair(stationSaved, technicalSaved)
    }

    override fun getAllFarmers() =
        farmerRepo.findAllByDeletedFalse().map { it.toResponseDto() }

    override fun getFarmerById(id: Long): FarmerPumpAggregateResponseDto {
        val entity = farmerRepo.findByIdOrNull(id)
            ?: throw RuntimeException("FarmerPumpAggregate topilmadi: $id")
        return entity.toResponseDto()
    }

    override fun updateFarmer(id: Long, dto: FarmerPumpAggregateDto): FarmerPumpAggregateResponseDto {
        val entity = farmerRepo.findByIdOrNull(id)
            ?: throw RuntimeException("FarmerPumpAggregate topilmadi: $id")

        entity.farmerName = dto.farmerName
        entity.inn = dto.inn
        entity.address = dto.address
        entity.pumpModel = dto.pumpModel
        entity.productionYear = dto.productionYear
        entity.waterLiftHeight = dto.waterLiftHeight
        entity.waterFlow = dto.waterFlow
        entity.enginePower = dto.enginePower
        entity.waterSource = dto.waterSource
        entity.connectedArea = dto.connectedArea
        entity.updatedAt = LocalDateTime.now()

        return farmerRepo.save(entity).toResponseDto()
    }

    override fun deleteFarmer(id: Long) {
        val entity = farmerRepo.findByIdOrNull(id)
            ?: throw RuntimeException("FarmerPumpAggregate topilmadi: $id")
        entity.deleted = true
        entity.updatedAt = LocalDateTime.now()
        farmerRepo.save(entity)
    }

    override fun getAllStations() =
        stationRepo.findAllByDeletedFalse().map { it.toResponseDto() }

    override fun getStationById(id: Long): PumpStationResponseDto {
        val entity = stationRepo.findByIdOrNull(id)
            ?: throw RuntimeException("PumpStation topilmadi: $id")
        return entity.toResponseDto()
    }

    override fun updateStation(id: Long, dto: PumpStationDto): PumpStationResponseDto {
        val entity = stationRepo.findByIdOrNull(id)
            ?: throw RuntimeException("PumpStation topilmadi: $id")

        entity.stationName = dto.stationName
        entity.installedAggregateCount = dto.installedAggregateCount
        entity.workingAggregateCount = dto.workingAggregateCount
        entity.waterSource = dto.waterSource
        entity.updatedAt = LocalDateTime.now()

        return stationRepo.save(entity).toResponseDto()
    }

    override fun deleteStation(id: Long) {
        val entity = stationRepo.findByIdOrNull(id)
            ?: throw RuntimeException("PumpStation topilmadi: $id")
        entity.deleted = true
        entity.updatedAt = LocalDateTime.now()
        stationRepo.save(entity)
    }

    override fun getAllTechnicals() =
        technicalRepo.findAllByDeletedFalse().map { it.toResponseDto() }

    override fun getTechnicalById(id: Long): PumpStationTechnicalResponseDto {
        val entity = technicalRepo.findByIdOrNull(id)
            ?: throw RuntimeException("PumpStationTechnical topilmadi: $id")
        return entity.toResponseDto()
    }

    override fun updateTechnical(id: Long, dto: PumpStationTechnicalDto): PumpStationTechnicalResponseDto {
        val entity = technicalRepo.findByIdOrNull(id)
            ?: throw RuntimeException("PumpStationTechnical topilmadi: $id")

        val station = dto.stationId?.let {
            stationRepo.findByIdOrNull(it)
                ?: throw RuntimeException("PumpStation topilmadi: $it")
        }

        entity.pumpStation = station
        entity.pumpModel = dto.pumpModel
        entity.electricEngineModel = dto.electricEngineModel
        entity.waterFlow = dto.waterFlow
        entity.enginePowerKw = dto.enginePowerKw
        entity.engineRotation = dto.engineRotation
        entity.waterLiftHeight = dto.waterLiftHeight
        entity.commissioningYear = dto.commissioningYear
        entity.attachedArea = dto.attachedArea
        entity.balanceValue = dto.balanceValue
        entity.pressurePipeDiameter = dto.pressurePipeDiameter
        entity.pressurePipeLength = dto.pressurePipeLength
        entity.pressurePipeTotalLength = dto.pressurePipeTotalLength
        entity.usedFarmers = dto.usedFarmers
        entity.inn = dto.inn
        entity.updatedAt = LocalDateTime.now()

        return technicalRepo.save(entity).toResponseDto()
    }

    override fun deleteTechnical(id: Long) {
        val entity = technicalRepo.findByIdOrNull(id)
            ?: throw RuntimeException("PumpStationTechnical topilmadi: $id")
        entity.deleted = true
        entity.updatedAt = LocalDateTime.now()
        technicalRepo.save(entity)
    }
}