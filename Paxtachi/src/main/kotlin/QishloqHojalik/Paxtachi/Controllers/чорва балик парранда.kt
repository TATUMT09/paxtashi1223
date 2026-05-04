package QishloqHojalik.Paxtachi.Controllers

import QishloqHojalik.Paxtachi.Dtos.*
import QishloqHojalik.Paxtachi.Services.FarmRepresentativeService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/farm-representatives")
class FarmRepresentativeController(
    private val service: FarmRepresentativeService
) {

    @PostMapping("/excel/import", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun importExcel(
        @RequestParam("file") file: MultipartFile
    ): ExcelImportResponse {
        return service.importExcel(file)
    }

    @PostMapping
    fun create(
        @RequestBody dto: FarmRepresentativeDto
    ): FarmRepresentativeResponseDto {
        return service.create(dto)
    }

    @GetMapping
    fun getAll(): List<FarmRepresentativeResponseDto> {
        return service.getAll()
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Long
    ): FarmRepresentativeResponseDto {
        return service.getById(id)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody dto: FarmRepresentativeDto
    ): FarmRepresentativeResponseDto {
        return service.update(id, dto)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long
    ) {
        service.delete(id)
    }
}