package QishloqHojalik.Paxtachi.Controllers


import QishloqHojalik.Paxtachi.Dtos.*
import QishloqHojalik.Paxtachi.Services.GreenhouseRecordService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/greenhouse-records")
class GreenhouseRecordController(
    private val service: GreenhouseRecordService
) {

    @PostMapping("/excel/import", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun importExcel(
        @RequestParam("file") file: MultipartFile
    ): ExcelImportResponse = service.importExcel(file)

    @PostMapping
    fun create(@RequestBody dto: GreenhouseRecordDto): GreenhouseRecordResponseDto =
        service.create(dto)

    @GetMapping
    fun getAll(): List<GreenhouseRecordResponseDto> =
        service.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): GreenhouseRecordResponseDto =
        service.getById(id)

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody dto: GreenhouseRecordDto
    ): GreenhouseRecordResponseDto =
        service.update(id, dto)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        service.delete(id)
    }
}