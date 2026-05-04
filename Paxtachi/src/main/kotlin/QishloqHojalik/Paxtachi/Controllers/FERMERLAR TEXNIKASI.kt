package QishloqHojalik.Paxtachi.Controllers

import QishloqHojalik.Paxtachi.Dtos.*
import QishloqHojalik.Paxtachi.Services.AgroTechnicService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/agro-technics")
class AgroTechnicController(
    private val service: AgroTechnicService
) {

    @PostMapping("/excel/import", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun importExcel(
        @RequestParam("file") file: MultipartFile
    ): ExcelImportResponse {
        return service.importExcel(file)
    }

    @PostMapping
    fun create(
        @RequestBody dto: AgroTechnicDto
    ): AgroTechnicResponseDto {
        return service.create(dto)
    }

    @GetMapping
    fun getAll(): List<AgroTechnicResponseDto> {
        return service.getAll()
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Long
    ): AgroTechnicResponseDto {
        return service.getById(id)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody dto: AgroTechnicDto
    ): AgroTechnicResponseDto {
        return service.update(id, dto)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long
    ) {
        service.delete(id)
    }
}