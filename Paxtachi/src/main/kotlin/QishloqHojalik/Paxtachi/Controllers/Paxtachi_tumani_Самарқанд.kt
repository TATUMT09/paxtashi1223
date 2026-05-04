package QishloqHojalik.Paxtachi.Controllers

import QishloqHojalik.Paxtachi.Dtos.CadastreLandRecordDto
import QishloqHojalik.Paxtachi.Dtos.CadastreLandRecordResponseDto
import QishloqHojalik.Paxtachi.Dtos.ExcelImportResponse
import QishloqHojalik.Paxtachi.Services.CadastreLandRecordService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/cadastre-land-records")
class CadastreLandRecordController(
    private val service: CadastreLandRecordService
) {

    @PostMapping("/excel/import", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun importExcel(
        @RequestParam("file") file: MultipartFile
    ): ExcelImportResponse {
        return service.importExcel(file)
    }

    @PostMapping
    fun create(
        @RequestBody dto: CadastreLandRecordDto
    ): CadastreLandRecordResponseDto {
        return service.create(dto)
    }

    @GetMapping
    fun getAll(): List<CadastreLandRecordResponseDto> {
        return service.getAll()
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Long
    ): CadastreLandRecordResponseDto {
        return service.getById(id)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody dto: CadastreLandRecordDto
    ): CadastreLandRecordResponseDto {
        return service.update(id, dto)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long
    ) {
        service.delete(id)
    }
}