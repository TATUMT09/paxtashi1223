package QishloqHojalik.Paxtachi.Controllers


import QishloqHojalik.Paxtachi.Dtos.*
import QishloqHojalik.Paxtachi.Services.LandContourService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/land-contours")
class LandContourController(
    private val service: LandContourService
) {

    @PostMapping("/excel/import", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun importExcel(
        @RequestParam("file") file: MultipartFile
    ): ExcelImportResponse {
        return service.importExcel(file)
    }

    @PostMapping
    fun create(@RequestBody dto: LandContourDto): LandContourResponseDto {
        return service.create(dto)
    }

    @GetMapping
    fun getAll(): List<LandContourResponseDto> {
        return service.getAll()
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): LandContourResponseDto {
        return service.getById(id)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody dto: LandContourDto
    ): LandContourResponseDto {
        return service.update(id, dto)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        service.delete(id)
    }
}