package QishloqHojalik.Paxtachi.Services
import QishloqHojalik.Paxtachi.Dtos.*
import QishloqHojalik.Paxtachi.Entity.GreenhouseRecord
import QishloqHojalik.Paxtachi.Repositories.GreenhouseRecordRepository
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime


interface GreenhouseRecordService {

    fun importExcel(file: MultipartFile): ExcelImportResponse

    fun create(dto: GreenhouseRecordDto): GreenhouseRecordResponseDto
    fun getAll(): List<GreenhouseRecordResponseDto>
    fun getById(id: Long): GreenhouseRecordResponseDto
    fun update(id: Long, dto: GreenhouseRecordDto): GreenhouseRecordResponseDto
    fun delete(id: Long)
}

@Service
class GreenhouseRecordServiceImpl(
    private val repo: GreenhouseRecordRepository
) : GreenhouseRecordService {

    override fun importExcel(file: MultipartFile): ExcelImportResponse {
        val workbook = WorkbookFactory.create(file.inputStream)
        val sheet = workbook.getSheetAt(0)

        var saved = 0
        val errors = mutableListOf<String>()

        try {
            for (i in 4..sheet.lastRowNum) {
                val row = sheet.getRow(i) ?: continue

                val ownerName = row.getCell(1).text()
                if (ownerName.isNullOrBlank()) continue

                // sariq umumiy/qishloq nomi qatorlarini o'tkazib yuborish
                if (row.getCell(2).text().isNullOrBlank() && row.getCell(6).text().isNullOrBlank()) continue

                try {
                    val entity = GreenhouseRecord(
                        ownerName = ownerName,
                        totalArea = row.getCell(2).doubleValue(),
                        contourNumber = row.getCell(3).text(),
                        greenhouseArea = row.getCell(4).doubleValue(),
                        cropName = row.getCell(5).text(),
                        passportSeries = row.getCell(6).text(),
                        jshshir = row.getCell(7).text(),
                        mfy = row.getCell(8).text(),
                        seedlingCount = row.getCell(9).intValue(),
                        requiredSeedlingCount = row.getCell(10).intValue(),
                        locationUrl = row.getCell(11).text(),
                        phoneNumber = row.getCell(12).text()
                    )

                    repo.save(entity)
                    saved++

                } catch (e: Exception) {
                    errors.add("Greenhouse row ${i + 1}: ${e.message}")
                }
            }
        } finally {
            workbook.close()
        }

        return ExcelImportResponse(
            greenhouseSaved = saved,
            errors = errors
        )
    }

    override fun create(dto: GreenhouseRecordDto): GreenhouseRecordResponseDto {
        return repo.save(
            GreenhouseRecord(
                ownerName = dto.ownerName,
                totalArea = dto.totalArea,
                contourNumber = dto.contourNumber,
                greenhouseArea = dto.greenhouseArea,
                cropName = dto.cropName,
                passportSeries = dto.passportSeries,
                jshshir = dto.jshshir,
                mfy = dto.mfy,
                seedlingCount = dto.seedlingCount,
                requiredSeedlingCount = dto.requiredSeedlingCount,
                locationUrl = dto.locationUrl,
                phoneNumber = dto.phoneNumber
            )
        ).toResponseDto()
    }

    override fun getAll(): List<GreenhouseRecordResponseDto> =
        repo.findAllByDeletedFalse().map { it.toResponseDto() }

    override fun getById(id: Long): GreenhouseRecordResponseDto {
        val entity = repo.findByIdAndDeletedFalse(id)
            ?: throw RuntimeException("Greenhouse record topilmadi: $id")
        return entity.toResponseDto()
    }

    override fun update(id: Long, dto: GreenhouseRecordDto): GreenhouseRecordResponseDto {
        val entity = repo.findByIdAndDeletedFalse(id)
            ?: throw RuntimeException("Greenhouse record topilmadi: $id")

        entity.ownerName = dto.ownerName
        entity.totalArea = dto.totalArea
        entity.contourNumber = dto.contourNumber
        entity.greenhouseArea = dto.greenhouseArea
        entity.cropName = dto.cropName
        entity.passportSeries = dto.passportSeries
        entity.jshshir = dto.jshshir
        entity.mfy = dto.mfy
        entity.seedlingCount = dto.seedlingCount
        entity.requiredSeedlingCount = dto.requiredSeedlingCount
        entity.locationUrl = dto.locationUrl
        entity.phoneNumber = dto.phoneNumber
        entity.updatedAt = LocalDateTime.now()

        return repo.save(entity).toResponseDto()
    }

    override fun delete(id: Long) {
        val entity = repo.findByIdAndDeletedFalse(id)
            ?: throw RuntimeException("Greenhouse record topilmadi: $id")

        entity.deleted = true
        entity.updatedAt = LocalDateTime.now()
        repo.save(entity)
    }
}