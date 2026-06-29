package QishloqHojalik.Paxtachi.Controllers

import QishloqHojalik.Paxtachi.Dtos.*
import QishloqHojalik.Paxtachi.Enums.Direction
import QishloqHojalik.Paxtachi.Security.AccessService
import QishloqHojalik.Paxtachi.Services.ChangeLogService
import QishloqHojalik.Paxtachi.Services.LandContourService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/land-contours")
class LandContourController(
    private val service: LandContourService,
    private val accessService: AccessService,
    private val changeLogService: ChangeLogService
) {

    private fun check(request: HttpServletRequest) {
        accessService.checkAccess(
            request,
            Direction.LAND_CONTOUR
        )
    }

    @PostMapping("/excel/import", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun importExcel(
        request: HttpServletRequest,
        @RequestParam("file") file: MultipartFile
    ): ExcelImportResponse {

        check(request)

        return service.importExcel(file)
    }

    @PostMapping
    fun create(
        request: HttpServletRequest,
        @RequestBody dto: LandContourDto
    ): LandContourResponseDto {

        check(request)

        val result = service.create(dto)
        changeLogService.log(request, "land_contours", result.id, "Yangi yer konturi qo'shildi", "CREATE")
        return result
    }

    @GetMapping
    fun getAll(
        request: HttpServletRequest
    ): List<LandContourResponseDto> {

        return service.getAll()
    }

    @GetMapping("/{id}")
    fun getById(
        request: HttpServletRequest,
        @PathVariable id: Long
    ): LandContourResponseDto {

        return service.getById(id)
    }

    @PutMapping("/{id}")
    fun update(
        request: HttpServletRequest,
        @PathVariable id: Long,
        @RequestBody dto: LandContourDto,
        @RequestParam(required = false, defaultValue = "") reason: String
    ): LandContourResponseDto {
        check(request)
        changeLogService.log(request, "land_contours", id, reason)
        return service.update(id, dto)
    }

    @DeleteMapping("/{id}")
    fun delete(
        request: HttpServletRequest,
        @PathVariable id: Long
    ) {

        check(request)

        changeLogService.log(request, "land_contours", id, "Yer konturi o'chirildi", "DELETE")
        service.delete(id)
    }
}