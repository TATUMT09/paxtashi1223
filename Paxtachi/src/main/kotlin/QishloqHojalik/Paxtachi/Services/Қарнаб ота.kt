package QishloqHojalik.Paxtachi.Services


import QishloqHojalik.Paxtachi.Dtos.*
import QishloqHojalik.Paxtachi.Entity.LandContour
import QishloqHojalik.Paxtachi.Repositories.LandContourRepository
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

interface LandContourService {

    fun importExcel(file: MultipartFile): ExcelImportResponse

    fun create(dto: LandContourDto): LandContourResponseDto
    fun getAll(): List<LandContourResponseDto>
    fun getById(id: Long): LandContourResponseDto
    fun update(id: Long, dto: LandContourDto): LandContourResponseDto
    fun delete(id: Long)
}

@Service
class LandContourServiceImpl(
    private val repo: LandContourRepository
) : LandContourService {

    override fun importExcel(file: MultipartFile): ExcelImportResponse {
        val workbook = WorkbookFactory.create(file.inputStream)
        val sheet = workbook.getSheetAt(0)

        var saved = 0
        val errors = mutableListOf<String>()

        try {
            for (i in 5..sheet.lastRowNum) {
                val row = sheet.getRow(i) ?: continue

                val contourNumber = row.getCell(0).text()
                if (contourNumber.isNullOrBlank()) continue

                try {
                    val entity = LandContour(
                        contourNumber = contourNumber,
                        totalArea = row.getCell(1).doubleValue(),

                        irrigatedAgricultureArea = row.getCell(2).doubleValue(),
                        irrigatedTomorqaArea = row.getCell(3).doubleValue(),

                        orchardArea = row.getCell(4).doubleValue(),
                        vineyardArea = row.getCell(5).doubleValue(),
                        mulberryArea = row.getCell(6).doubleValue(),
                        nurseryArea = row.getCell(7).doubleValue(),
                        meliorationArea = row.getCell(8).doubleValue(),

                        grayLandAgricultureArea = row.getCell(9).doubleValue(),
                        grayLandPastureArea = row.getCell(10).doubleValue(),

                        commonYardArea = row.getCell(11).doubleValue(),

                        agricultureTotalArea = row.getCell(12).doubleValue(),
                        agricultureArableArea = row.getCell(13).doubleValue(),
                        agricultureIrrigatedArea = row.getCell(14).doubleValue(),

                        tomorqaTotalArea = row.getCell(15).doubleValue(),
                        tomorqaInsideSettlementArea = row.getCell(16).doubleValue(),

                        treePlantationArea = row.getCell(17).doubleValue(),
                        pastureArea = row.getCell(18).doubleValue(),
                        lakeFishArea = row.getCell(19).doubleValue(),
                        publicBuildingsArea = row.getCell(20).doubleValue(),
                        streetsArea = row.getCell(21).doubleValue(),
                        cemeteryArea = row.getCell(22).doubleValue(),
                        constructionArea = row.getCell(23).doubleValue(),
                        forestArea = row.getCell(24).doubleValue(),
                        canalsArea = row.getCell(25).doubleValue(),
                        roadsArea = row.getCell(26).doubleValue(),
                        otherLandsArea = row.getCell(27).doubleValue(),
                        buildingUnderArea = row.getCell(28).doubleValue(),
                        agriculturalServiceArea = row.getCell(29).doubleValue(),
                        cottonReceptionArea = row.getCell(30).doubleValue(),
                        abandonedFarmBuildingArea = row.getCell(31).doubleValue(),
                        auxiliaryFarmArea = row.getCell(32).doubleValue()
                    )

                    repo.save(entity)
                    saved++

                } catch (e: Exception) {
                    errors.add("LandContour row ${i + 1}: ${e.message}")
                }
            }
        } finally {
            workbook.close()
        }

        return ExcelImportResponse(
            landContourSaved = saved,
            errors = errors
        )
    }

    override fun create(dto: LandContourDto): LandContourResponseDto {
        val entity = LandContour(
            contourNumber = dto.contourNumber,
            totalArea = dto.totalArea,
            irrigatedAgricultureArea = dto.irrigatedAgricultureArea,
            irrigatedTomorqaArea = dto.irrigatedTomorqaArea,
            orchardArea = dto.orchardArea,
            vineyardArea = dto.vineyardArea,
            mulberryArea = dto.mulberryArea,
            nurseryArea = dto.nurseryArea,
            meliorationArea = dto.meliorationArea,
            grayLandAgricultureArea = dto.grayLandAgricultureArea,
            grayLandPastureArea = dto.grayLandPastureArea,
            commonYardArea = dto.commonYardArea,
            agricultureTotalArea = dto.agricultureTotalArea,
            agricultureArableArea = dto.agricultureArableArea,
            agricultureIrrigatedArea = dto.agricultureIrrigatedArea,
            tomorqaTotalArea = dto.tomorqaTotalArea,
            tomorqaInsideSettlementArea = dto.tomorqaInsideSettlementArea,
            treePlantationArea = dto.treePlantationArea,
            pastureArea = dto.pastureArea,
            lakeFishArea = dto.lakeFishArea,
            publicBuildingsArea = dto.publicBuildingsArea,
            streetsArea = dto.streetsArea,
            cemeteryArea = dto.cemeteryArea,
            constructionArea = dto.constructionArea,
            forestArea = dto.forestArea,
            canalsArea = dto.canalsArea,
            roadsArea = dto.roadsArea,
            otherLandsArea = dto.otherLandsArea,
            buildingUnderArea = dto.buildingUnderArea,
            agriculturalServiceArea = dto.agriculturalServiceArea,
            cottonReceptionArea = dto.cottonReceptionArea,
            abandonedFarmBuildingArea = dto.abandonedFarmBuildingArea,
            auxiliaryFarmArea = dto.auxiliaryFarmArea
        )

        return repo.save(entity).toResponseDto()
    }

    override fun getAll(): List<LandContourResponseDto> =
        repo.findAllByDeletedFalse().map { it.toResponseDto() }

    override fun getById(id: Long): LandContourResponseDto {
        val entity = repo.findByIdAndDeletedFalse(id)
            ?: throw RuntimeException("LandContour topilmadi: $id")
        return entity.toResponseDto()
    }

    override fun update(id: Long, dto: LandContourDto): LandContourResponseDto {
        val entity = repo.findByIdAndDeletedFalse(id)
            ?: throw RuntimeException("LandContour topilmadi: $id")

        entity.contourNumber = dto.contourNumber
        entity.totalArea = dto.totalArea
        entity.irrigatedAgricultureArea = dto.irrigatedAgricultureArea
        entity.irrigatedTomorqaArea = dto.irrigatedTomorqaArea
        entity.orchardArea = dto.orchardArea
        entity.vineyardArea = dto.vineyardArea
        entity.mulberryArea = dto.mulberryArea
        entity.nurseryArea = dto.nurseryArea
        entity.meliorationArea = dto.meliorationArea
        entity.grayLandAgricultureArea = dto.grayLandAgricultureArea
        entity.grayLandPastureArea = dto.grayLandPastureArea
        entity.commonYardArea = dto.commonYardArea
        entity.agricultureTotalArea = dto.agricultureTotalArea
        entity.agricultureArableArea = dto.agricultureArableArea
        entity.agricultureIrrigatedArea = dto.agricultureIrrigatedArea
        entity.tomorqaTotalArea = dto.tomorqaTotalArea
        entity.tomorqaInsideSettlementArea = dto.tomorqaInsideSettlementArea
        entity.treePlantationArea = dto.treePlantationArea
        entity.pastureArea = dto.pastureArea
        entity.lakeFishArea = dto.lakeFishArea
        entity.publicBuildingsArea = dto.publicBuildingsArea
        entity.streetsArea = dto.streetsArea
        entity.cemeteryArea = dto.cemeteryArea
        entity.constructionArea = dto.constructionArea
        entity.forestArea = dto.forestArea
        entity.canalsArea = dto.canalsArea
        entity.roadsArea = dto.roadsArea
        entity.otherLandsArea = dto.otherLandsArea
        entity.buildingUnderArea = dto.buildingUnderArea
        entity.agriculturalServiceArea = dto.agriculturalServiceArea
        entity.cottonReceptionArea = dto.cottonReceptionArea
        entity.abandonedFarmBuildingArea = dto.abandonedFarmBuildingArea
        entity.auxiliaryFarmArea = dto.auxiliaryFarmArea
        entity.updatedAt = LocalDateTime.now()

        return repo.save(entity).toResponseDto()
    }

    override fun delete(id: Long) {
        val entity = repo.findByIdAndDeletedFalse(id)
            ?: throw RuntimeException("LandContour topilmadi: $id")

        entity.deleted = true
        entity.updatedAt = LocalDateTime.now()
        repo.save(entity)
    }
}