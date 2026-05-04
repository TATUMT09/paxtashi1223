package QishloqHojalik.Paxtachi.Controllers

import QishloqHojalik.Paxtachi.Entity.PaxtachiBalance
import QishloqHojalik.Paxtachi.Enums.Specialization
import QishloqHojalik.Paxtachi.Services.PaxtachiBalanceService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/farmers")
class FarmersController(
    private val service: PaxtachiBalanceService
) {

    @GetMapping
    fun getFarmers(
        @RequestParam(required = false) specializationId: String?,
        @RequestParam(required = false) q: String?,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "20") pageSize: Int,
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
        @PathVariable id: Long,
        @RequestParam(required = false) season: Int?
    ): Map<String, Any> {
        return service.getFarmerAnalytics(id, season)
    }

    @PostMapping("/upload")
    fun uploadExcel(
        @RequestParam("file") file: MultipartFile
    ): Map<String, String> {
        val result = service.uploadExcel(file)
        return mapOf("message" to result)
    }

    @GetMapping("/hudud-section")
    fun getHududSection(
        @RequestParam name: String
    ): List<PaxtachiBalance> {
        return service.getHududSection(name)
    }
}

@RestController
@RequestMapping("/api/v1/dashboard")
class DashboardController(
    private val service: PaxtachiBalanceService
) {

    @GetMapping("/land-summary")
    fun landSummary(
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
    private val service: PaxtachiBalanceService
) {

    @GetMapping("/summary")
    fun getSummary(
        @RequestParam(required = false) season: Int?
    ): Map<String, Any> {
        return service.getSpecializationSummary(season)
    }
}