package QishloqHojalik.Paxtachi.Controllers

import QishloqHojalik.Paxtachi.Dtos.*
import QishloqHojalik.Paxtachi.Enums.Direction
import QishloqHojalik.Paxtachi.Security.AccessService
import QishloqHojalik.Paxtachi.Services.AgroTechnicService
import QishloqHojalik.Paxtachi.Services.ChangeLogService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/agro-technics")
class AgroTechnicController(
    private val service: AgroTechnicService,
    private val accessService: AccessService,
    private val changeLogService: ChangeLogService
) {

    private fun check(request: HttpServletRequest) {
        accessService.checkAccess(
            request,
            Direction.AGRO_TECHNIC
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
        @RequestBody dto: AgroTechnicDto
    ): AgroTechnicResponseDto {

        check(request)

        val result = service.create(dto)
        changeLogService.log(request, "agro_technics", result.id, "Yangi texnika qo'shildi", "CREATE")
        return result
    }

    @GetMapping
    fun getAll(
        request: HttpServletRequest
    ): List<AgroTechnicResponseDto> {


        return service.getAll()
    }

    @GetMapping("/{id}")
    fun getById(
        request: HttpServletRequest,
        @PathVariable id: Long
    ): AgroTechnicResponseDto {


        return service.getById(id)
    }

    @PutMapping("/{id}")
    fun update(
        request: HttpServletRequest,
        @PathVariable id: Long,
        @RequestBody dto: AgroTechnicDto,
        @RequestParam(required = false, defaultValue = "") reason: String
    ): AgroTechnicResponseDto {

        check(request)

        changeLogService.log(request, "agro_technics", id, reason)
        return service.update(id, dto)
    }

    @DeleteMapping("/{id}")
    fun delete(
        request: HttpServletRequest,
        @PathVariable id: Long
    ) {

        check(request)

        changeLogService.log(request, "agro_technics", id, "Texnika o'chirildi", "DELETE")
        service.delete(id)
    }
}