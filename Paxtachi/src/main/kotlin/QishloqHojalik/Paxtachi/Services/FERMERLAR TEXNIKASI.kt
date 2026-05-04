package QishloqHojalik.Paxtachi.Services

import QishloqHojalik.Paxtachi.Dtos.*
import QishloqHojalik.Paxtachi.Entity.AgroTechnic
import QishloqHojalik.Paxtachi.Repositories.AgroTechnicRepository
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

interface AgroTechnicService {

    fun importExcel(file: MultipartFile): ExcelImportResponse

    fun create(dto: AgroTechnicDto): AgroTechnicResponseDto

    fun getAll(): List<AgroTechnicResponseDto>

    fun getById(id: Long): AgroTechnicResponseDto

    fun update(id: Long, dto: AgroTechnicDto): AgroTechnicResponseDto

    fun delete(id: Long)
}

@Service
class AgroTechnicServiceImpl(
    private val repo: AgroTechnicRepository
) : AgroTechnicService {

    // 🔥 EXCEL IMPORT
    override fun importExcel(file: MultipartFile): ExcelImportResponse {

        val workbook = WorkbookFactory.create(file.inputStream)
        val sheet = workbook.getSheetAt(0)

        var saved = 0
        val errors = mutableListOf<String>()

        try {
            for (i in 2..sheet.lastRowNum) {

                val row = sheet.getRow(i) ?: continue

                val ownerName = row.getCell(1).text()
                if (ownerName.isNullOrBlank()) continue

                try {
                    val entity = AgroTechnic(
                        ownerName = ownerName,
                        ownershipType = row.getCell(2).text(),
                        inn = row.getCell(3).text(),
                        district = row.getCell(4).text(),
                        workType = row.getCell(5).text(),
                        model = row.getCell(6).text(),
                        technicType = row.getCell(7).text(),
                        productionYear = row.getCell(8).intValue(),
                        engineNumber = row.getCell(9).text(),
                        enginePower = row.getCell(10).intValue(),
                        chassisNumber = row.getCell(11).text(),
                        color = row.getCell(12).text(),
                        regionCode = row.getCell(13).text(),
                        series = row.getCell(14).text(),
                        number = row.getCell(15).text()
                    )

                    repo.save(entity)
                    saved++

                } catch (e: Exception) {
                    errors.add("Row ${i + 1}: ${e.message}")
                }
            }

        } finally {
            workbook.close()
        }

        return ExcelImportResponse(
            errors = errors
        )
    }

    // ➕ CREATE
    override fun create(dto: AgroTechnicDto): AgroTechnicResponseDto {

        val entity = AgroTechnic(
            ownerName = dto.ownerName,
            ownershipType = dto.ownershipType,
            inn = dto.inn,
            district = dto.district,
            workType = dto.workType,
            model = dto.model,
            technicType = dto.technicType,
            productionYear = dto.productionYear,
            engineNumber = dto.engineNumber,
            enginePower = dto.enginePower,
            chassisNumber = dto.chassisNumber,
            color = dto.color,
            regionCode = dto.regionCode,
            series = dto.series,
            number = dto.number
        )

        return repo.save(entity).toResponseDto()
    }

    // 📥 GET ALL
    override fun getAll(): List<AgroTechnicResponseDto> {
        return repo.findAllByDeletedFalse().map { it.toResponseDto() }
    }

    // 🔍 GET BY ID
    override fun getById(id: Long): AgroTechnicResponseDto {

        val entity = repo.findByIdAndDeletedFalse(id)
            ?: throw RuntimeException("AgroTechnic topilmadi: $id")

        return entity.toResponseDto()
    }

    // ✏️ UPDATE
    override fun update(id: Long, dto: AgroTechnicDto): AgroTechnicResponseDto {

        val entity = repo.findByIdAndDeletedFalse(id)
            ?: throw RuntimeException("AgroTechnic topilmadi: $id")

        entity.ownerName = dto.ownerName
        entity.ownershipType = dto.ownershipType
        entity.inn = dto.inn
        entity.district = dto.district
        entity.workType = dto.workType
        entity.model = dto.model
        entity.technicType = dto.technicType
        entity.productionYear = dto.productionYear
        entity.engineNumber = dto.engineNumber
        entity.enginePower = dto.enginePower
        entity.chassisNumber = dto.chassisNumber
        entity.color = dto.color
        entity.regionCode = dto.regionCode
        entity.series = dto.series
        entity.number = dto.number
        entity.updatedAt = LocalDateTime.now()

        return repo.save(entity).toResponseDto()
    }

    // 🗑 DELETE (SOFT DELETE)
    override fun delete(id: Long) {

        val entity = repo.findByIdAndDeletedFalse(id)
            ?: throw RuntimeException("AgroTechnic topilmadi: $id")

        entity.deleted = true
        entity.updatedAt = LocalDateTime.now()

        repo.save(entity)
    }
}