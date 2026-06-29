package QishloqHojalik.Paxtachi.Controllers

import QishloqHojalik.Paxtachi.Dtos.FarmerUpdateDto
import QishloqHojalik.Paxtachi.Entity.PaxtachiBalance
import QishloqHojalik.Paxtachi.Enums.Direction
import QishloqHojalik.Paxtachi.Enums.Specialization
import QishloqHojalik.Paxtachi.Security.AccessService
import QishloqHojalik.Paxtachi.Services.ChangeLogService
import QishloqHojalik.Paxtachi.Services.PaxtachiBalanceService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/farmers")
class FarmersController(
    private val service: PaxtachiBalanceService,
    private val accessService: AccessService,
    private val changeLogService: ChangeLogService
) {

    private fun checkAdmin(request: HttpServletRequest) {
        val role = request.getAttribute("role") as? String
            ?: throw RuntimeException("Token ichida role yo'q")
        if (role != "SUPER_ADMIN" && role != "ADMIN")
            throw RuntimeException("Faqat admin tahrirlashi mumkin")
    }

    private fun check(request: HttpServletRequest) {
        accessService.checkAccess(
            request,
            Direction.FARMERS,
            Direction.PAXTACHILIK,
            Direction.TUTCHILIK,
            Direction.GALLACHILIK,
            Direction.BOGDORCHILIK
        )
    }

    @GetMapping
    fun getFarmers(
        request: HttpServletRequest,
        @RequestParam(required = false) specializationId: String?,
        @RequestParam(required = false) q: String?,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "1000") pageSize: Int,
        @RequestParam(defaultValue = "id:desc") sort: String,
        @RequestParam(required = false) season: Int?
    ): Map<String, Any> {


        val safePage = if (page <= 0) 1 else page
        val safePageSize = if (pageSize <= 0) 20 else pageSize

        return service.getFarmers(
            specializationId,
            q,
            safePage,
            safePageSize,
            sort,
            season
        )
    }

    @GetMapping("/{id}/analytics")
    fun getFarmerAnalytics(
        request: HttpServletRequest,
        @PathVariable id: Long,
        @RequestParam(required = false) season: Int?
    ): Map<String, Any> {


        return service.getFarmerAnalytics(id, season)
    }

    @PostMapping("/upload")
    fun uploadExcel(
        request: HttpServletRequest,
        @RequestParam("file") file: MultipartFile
    ): Map<String, String> {

        check(request)

        val result = service.uploadExcel(file)

        return mapOf("message" to result)
    }

    @GetMapping("/{id}")
    fun getById(
        request: HttpServletRequest,
        @PathVariable id: Long
    ): PaxtachiBalance {
        return service.getById(id)
    }

    @PutMapping("/{id}")
    fun update(
        request: HttpServletRequest,
        @PathVariable id: Long,
        @RequestBody dto: FarmerUpdateDto,
        @RequestParam(required = false, defaultValue = "") reason: String
    ): PaxtachiBalance {
        checkAdmin(request)
        changeLogService.log(request, "paxtachi_balance", id, reason, "UPDATE")
        return service.updateFarmer(id, dto)
    }

    @DeleteMapping("/{id}")
    fun delete(
        request: HttpServletRequest,
        @PathVariable id: Long,
        @RequestParam(required = false, defaultValue = "") reason: String
    ) {
        checkAdmin(request)
        changeLogService.log(request, "paxtachi_balance", id, reason, "DELETE")
        service.deleteFarmer(id)
    }

    @GetMapping("/hudud-section")
    fun getHududSection(
        request: HttpServletRequest,
        @RequestParam name: String
    ): List<PaxtachiBalance> {
        return service.getHududSection(name)
    }
}

@RestController
@RequestMapping("/api/v1/dashboard")
class DashboardController(
    private val service: PaxtachiBalanceService,
    private val accessService: AccessService
) {

    private fun check(request: HttpServletRequest) {
        accessService.checkAccess(
            request,
            Direction.DASHBOARD
        )
    }

    @GetMapping("/land-summary")
    fun landSummary(
        request: HttpServletRequest,
        @RequestParam(required = false) season: Int?
    ): PaxtachiBalance {


        return service.getDashboardSummary(season)
    }
}

@RestController
@RequestMapping("/api/v1/lookups")
class LookupController {

    @GetMapping("/specializations")
    fun getSpecializations(): List<Map<String, String>> {

        return Specialization.values().map {
            mapOf(
                "id" to it.name,
                "name" to it.nameUz,
                "icon" to it.icon
            )
        }
    }
}

@RestController
@RequestMapping("/api/v1/specializations")
class SpecializationController(
    private val service: PaxtachiBalanceService,
    private val accessService: AccessService
) {

    private fun check(request: HttpServletRequest) {
        accessService.checkAccess(
            request,
            Direction.SPECIALIZATION
        )
    }

    @GetMapping("/summary")
    fun getSummary(
        request: HttpServletRequest,
        @RequestParam(required = false) season: Int?
    ): Map<String, Any> {


        return service.getSpecializationSummary(season)
    }
}