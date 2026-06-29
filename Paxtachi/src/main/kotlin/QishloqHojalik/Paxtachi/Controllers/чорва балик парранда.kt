package QishloqHojalik.Paxtachi.Controllers

import QishloqHojalik.Paxtachi.Dtos.*
import QishloqHojalik.Paxtachi.Enums.Direction
import QishloqHojalik.Paxtachi.Security.AccessService
import QishloqHojalik.Paxtachi.Services.ChangeLogService
import QishloqHojalik.Paxtachi.Services.FarmRepresentativeService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/farm-representatives")
class FarmRepresentativeController(
    private val service: FarmRepresentativeService,
    private val accessService: AccessService,
    private val changeLogService: ChangeLogService
) {

    private fun check(request: HttpServletRequest) {
        accessService.checkAccess(
            request,
            Direction.FARM_REPRESENTATIVE,
            Direction.CHORVACHILIK
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
        @RequestBody dto: FarmRepresentativeDto
    ): FarmRepresentativeResponseDto {

        check(request)

        val result = service.create(dto)
        changeLogService.log(request, "farm_representatives", result.id, "Yangi fermer vakili qo'shildi", "CREATE")
        return result
    }

    @GetMapping
    fun getAll(
        request: HttpServletRequest
    ): List<FarmRepresentativeResponseDto> {


        return service.getAll()
    }

    @GetMapping("/{id}")
    fun getById(
        request: HttpServletRequest,
        @PathVariable id: Long
    ): FarmRepresentativeResponseDto {


        return service.getById(id)
    }

    @PutMapping("/{id}")
    fun update(
        request: HttpServletRequest,
        @PathVariable id: Long,
        @RequestBody dto: FarmRepresentativeDto,
        @RequestParam(required = false, defaultValue = "") reason: String
    ): FarmRepresentativeResponseDto {

        check(request)

        changeLogService.log(request, "farm_representatives", id, reason)
        return service.update(id, dto)
    }

    @DeleteMapping("/{id}")
    fun delete(
        request: HttpServletRequest,
        @PathVariable id: Long
    ) {

        check(request)

        changeLogService.log(request, "farm_representatives", id, "Fermer vakili o'chirildi", "DELETE")
        service.delete(id)
    }
}