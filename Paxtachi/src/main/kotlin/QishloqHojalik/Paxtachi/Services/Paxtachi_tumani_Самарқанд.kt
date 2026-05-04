package QishloqHojalik.Paxtachi.Services

import QishloqHojalik.Paxtachi.Dtos.CadastreLandRecordResponseDto
import QishloqHojalik.Paxtachi.Dtos.ExcelImportResponse
import QishloqHojalik.Paxtachi.Dtos.*
import QishloqHojalik.Paxtachi.Entity.CadastreLandRecord
import QishloqHojalik.Paxtachi.Repositories.CadastreLandRecordRepository
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime


interface CadastreLandRecordService {

    fun importExcel(file: MultipartFile): ExcelImportResponse

    fun getAll(): List<CadastreLandRecordResponseDto>
    fun getById(id: Long): CadastreLandRecordResponseDto

    fun create(dto: CadastreLandRecordDto): CadastreLandRecordResponseDto
    fun update(id: Long, dto: CadastreLandRecordDto): CadastreLandRecordResponseDto
    fun delete(id: Long)
}


@Service
class CadastreLandRecordServiceImpl(
    private val repo: CadastreLandRecordRepository
) : CadastreLandRecordService {

    // 🔥 EXCEL IMPORT
    override fun importExcel(file: MultipartFile): ExcelImportResponse {

        val workbook = XSSFWorkbook(file.inputStream)
        val sheet = workbook.getSheetAt(0)

        var saved = 0
        val errors = mutableListOf<String>()

        try {
            for (i in 2..sheet.lastRowNum) {

                val row = sheet.getRow(i) ?: continue

                val region = row.getCell(1).text()
                val district = row.getCell(2).text()

                if (region.isNullOrBlank() && district.isNullOrBlank()) continue

                try {
                    val entity = CadastreLandRecord(
                        regionName = region,
                        districtName = district,
                        neighborhoodName = row.getCell(3).text(),

                        contourNumber = row.getCell(4).text(),
                        area = row.getCell(5).doubleValue(),

                        landType = row.getCell(6).text(),
                        farmName = row.getCell(7).text(),

                        landNumber = row.getCell(8).text(),
                        cadastreNumber = row.getCell(9).text(),

                        cadastralRegistered = row.getCell(10).intValue(),
                        notRegisteredReason = row.getCell(11).text(),

                        problem = row.getCell(15).text(),
                        problemDescription = row.getCell(16).text()
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
            cadastreSaved = saved,
            errors = errors
        )
    }

    // 🔍 GET ALL
    override fun getAll(): List<CadastreLandRecordResponseDto> {
        return repo.findAllByDeletedFalse().map { it.toResponseDto() }
    }

    // 🔍 GET BY ID
    override fun getById(id: Long): CadastreLandRecordResponseDto {
        val entity = repo.findByIdAndDeletedFalse(id)
            ?: throw RuntimeException("Cadastre topilmadi: $id")

        return entity.toResponseDto()
    }

    // ➕ CREATE
    override fun create(dto: CadastreLandRecordDto): CadastreLandRecordResponseDto {

        val entity = CadastreLandRecord(
            regionName = dto.regionName,
            districtName = dto.districtName,
            neighborhoodName = dto.neighborhoodName,
            contourNumber = dto.contourNumber,
            area = dto.area,
            landType = dto.landType,
            farmName = dto.farmName,
            landNumber = dto.landNumber,
            cadastreNumber = dto.cadastreNumber,
            cadastralRegistered = dto.cadastralRegistered,
            notRegisteredReason = dto.notRegisteredReason,
            problem = dto.problem,
            problemDescription = dto.problemDescription
        )

        return repo.save(entity).toResponseDto()
    }

    // ✏️ UPDATE
    override fun update(id: Long, dto: CadastreLandRecordDto): CadastreLandRecordResponseDto {

        val entity = repo.findByIdAndDeletedFalse(id)
            ?: throw RuntimeException("Cadastre topilmadi: $id")

        entity.regionName = dto.regionName
        entity.districtName = dto.districtName
        entity.neighborhoodName = dto.neighborhoodName
        entity.contourNumber = dto.contourNumber
        entity.area = dto.area
        entity.landType = dto.landType
        entity.farmName = dto.farmName
        entity.landNumber = dto.landNumber
        entity.cadastreNumber = dto.cadastreNumber
        entity.cadastralRegistered = dto.cadastralRegistered
        entity.notRegisteredReason = dto.notRegisteredReason
        entity.problem = dto.problem
        entity.problemDescription = dto.problemDescription
        entity.updatedAt = LocalDateTime.now()

        return repo.save(entity).toResponseDto()
    }

    // 🗑 DELETE (SOFT DELETE)
    override fun delete(id: Long) {

        val entity = repo.findByIdAndDeletedFalse(id)
            ?: throw RuntimeException("Cadastre topilmadi: $id")

        entity.deleted = true
        entity.updatedAt = LocalDateTime.now()

        repo.save(entity)
    }
}