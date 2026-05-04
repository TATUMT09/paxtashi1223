package QishloqHojalik.Paxtachi.Services


import QishloqHojalik.Paxtachi.Dtos.*
import org.springframework.web.multipart.MultipartFile
import QishloqHojalik.Paxtachi.Entity.FarmRepresentative
import QishloqHojalik.Paxtachi.Repositories.FarmRepresentativeRepository
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface FarmRepresentativeService {

    fun importExcel(file: MultipartFile): ExcelImportResponse

    fun create(dto: FarmRepresentativeDto): FarmRepresentativeResponseDto
    fun getAll(): List<FarmRepresentativeResponseDto>
    fun getById(id: Long): FarmRepresentativeResponseDto
    fun update(id: Long, dto: FarmRepresentativeDto): FarmRepresentativeResponseDto
    fun delete(id: Long)
}



@Service
class FarmRepresentativeServiceImpl(
    private val repo: FarmRepresentativeRepository
) : FarmRepresentativeService {

    override fun importExcel(file: MultipartFile): ExcelImportResponse {
        val workbook = WorkbookFactory.create(file.inputStream)
        val sheet = workbook.getSheetAt(0)

        var saved = 0
        val errors = mutableListOf<String>()

        try {
            for (i in 5..sheet.lastRowNum) {
                val row = sheet.getRow(i) ?: continue

                val inn = row.getCell(1).text()
                val farmName = row.getCell(2).text()

                if (inn.isNullOrBlank() && farmName.isNullOrBlank()) continue

                try {
                    val entity = FarmRepresentative(
                        inn = inn,
                        farmName = farmName,
                        activityType = row.getCell(3).text(),
                        farmerFullName = row.getCell(4).text(),
                        farmerPhone = row.getCell(5).text(),
                        livestockCount = row.getCell(6).doubleValue(),
                        landManagerFullName = row.getCell(7).text(),
                        landManagerPhone = row.getCell(8).text()
                    )

                    repo.save(entity)
                    saved++

                } catch (e: Exception) {
                    errors.add("FarmRepresentative row ${i + 1}: ${e.message}")
                }
            }
        } finally {
            workbook.close()
        }

        return ExcelImportResponse(
            farmRepresentativeSaved = saved,
            errors = errors
        )
    }

    override fun create(dto: FarmRepresentativeDto): FarmRepresentativeResponseDto {
        val entity = FarmRepresentative(
            inn = dto.inn,
            farmName = dto.farmName,
            activityType = dto.activityType,
            farmerFullName = dto.farmerFullName,
            farmerPhone = dto.farmerPhone,
            livestockCount = dto.livestockCount,
            landManagerFullName = dto.landManagerFullName,
            landManagerPhone = dto.landManagerPhone
        )

        return repo.save(entity).toResponseDto()
    }

    override fun getAll(): List<FarmRepresentativeResponseDto> {
        return repo.findAllByDeletedFalse().map { it.toResponseDto() }
    }

    override fun getById(id: Long): FarmRepresentativeResponseDto {
        val entity = repo.findByIdAndDeletedFalse(id)
            ?: throw RuntimeException("FarmRepresentative topilmadi: $id")

        return entity.toResponseDto()
    }

    override fun update(id: Long, dto: FarmRepresentativeDto): FarmRepresentativeResponseDto {
        val entity = repo.findByIdAndDeletedFalse(id)
            ?: throw RuntimeException("FarmRepresentative topilmadi: $id")

        entity.inn = dto.inn
        entity.farmName = dto.farmName
        entity.activityType = dto.activityType
        entity.farmerFullName = dto.farmerFullName
        entity.farmerPhone = dto.farmerPhone
        entity.livestockCount = dto.livestockCount
        entity.landManagerFullName = dto.landManagerFullName
        entity.landManagerPhone = dto.landManagerPhone
        entity.updatedAt = LocalDateTime.now()

        return repo.save(entity).toResponseDto()
    }

    override fun delete(id: Long) {
        val entity = repo.findByIdAndDeletedFalse(id)
            ?: throw RuntimeException("FarmRepresentative topilmadi: $id")

        entity.deleted = true
        entity.updatedAt = LocalDateTime.now()
        repo.save(entity)
    }
}