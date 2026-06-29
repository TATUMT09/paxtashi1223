package QishloqHojalik.Paxtachi.Controllers

import QishloqHojalik.Paxtachi.Dtos.*
import QishloqHojalik.Paxtachi.Enums.Direction
import QishloqHojalik.Paxtachi.Security.AccessService
import QishloqHojalik.Paxtachi.Services.ChangeLogService
import QishloqHojalik.Paxtachi.Services.PumpExcelService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/pump")
class PumpController(
    private val service: PumpExcelService,
    private val accessService: AccessService,
    private val changeLogService: ChangeLogService
) {

    private fun check(request: HttpServletRequest) {
        accessService.checkAccess(request, Direction.PUMP)
    }

    // ─── Excel import ─────────────────────────────────────────────────────────
    @PostMapping("/excel/import", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun importExcel(
        request: HttpServletRequest,
        @RequestParam("file") file: MultipartFile
    ): ExcelImportResponse {
        check(request)
        return service.importExcel(file)
    }

    // ─── Farmer aggregates ────────────────────────────────────────────────────
    @PostMapping("/farmers")
    fun createFarmer(
        request: HttpServletRequest,
        @RequestBody dto: FarmerPumpAggregateDto
    ): FarmerPumpAggregateResponseDto {
        check(request)
        val result = service.createFarmer(dto)
        changeLogService.log(request, "farmer_pump_aggregates", result.id, "Yangi fermer qo'shildi", "CREATE")
        return result
    }

    @GetMapping("/farmers")
    fun getAllFarmers(): List<FarmerPumpAggregateResponseDto> = service.getAllFarmers()

    @GetMapping("/farmers/{id}")
    fun getFarmerById(@PathVariable id: Long): FarmerPumpAggregateResponseDto = service.getFarmerById(id)

    @PutMapping("/farmers/{id}")
    fun updateFarmer(
        request: HttpServletRequest,
        @PathVariable id: Long,
        @RequestBody dto: FarmerPumpAggregateDto,
        @RequestParam(required = false, defaultValue = "") reason: String
    ): FarmerPumpAggregateResponseDto {
        check(request)
        changeLogService.log(request, "farmer_pump_aggregates", id, reason)
        return service.updateFarmer(id, dto)
    }

    @DeleteMapping("/farmers/{id}")
    fun deleteFarmer(request: HttpServletRequest, @PathVariable id: Long) {
        check(request)
        changeLogService.log(request, "farmer_pump_aggregates", id, "Fermer o'chirildi", "DELETE")
        service.deleteFarmer(id)
    }

    // ─── Pump stations ────────────────────────────────────────────────────────
    @PostMapping("/stations")
    fun createStation(
        request: HttpServletRequest,
        @RequestBody dto: PumpStationDto
    ): PumpStationResponseDto {
        check(request)
        val result = service.createStation(dto)
        changeLogService.log(request, "pump_stations", result.id, "Yangi stansiya qo'shildi", "CREATE")
        return result
    }

    @GetMapping("/stations")
    fun getAllStations(): List<PumpStationResponseDto> = service.getAllStations()

    @GetMapping("/stations/{id}")
    fun getStationById(@PathVariable id: Long): PumpStationResponseDto = service.getStationById(id)

    @PutMapping("/stations/{id}")
    fun updateStation(
        request: HttpServletRequest,
        @PathVariable id: Long,
        @RequestBody dto: PumpStationDto,
        @RequestParam(required = false, defaultValue = "") reason: String
    ): PumpStationResponseDto {
        check(request)
        changeLogService.log(request, "pump_stations", id, reason)
        return service.updateStation(id, dto)
    }

    @DeleteMapping("/stations/{id}")
    fun deleteStation(request: HttpServletRequest, @PathVariable id: Long) {
        check(request)
        changeLogService.log(request, "pump_stations", id, "Stansiya o'chirildi", "DELETE")
        service.deleteStation(id)
    }

    // ─── Station technicals ───────────────────────────────────────────────────
    @PostMapping("/technicals")
    fun createTechnical(
        request: HttpServletRequest,
        @RequestBody dto: PumpStationTechnicalDto
    ): PumpStationTechnicalResponseDto {
        check(request)
        val result = service.createTechnical(dto)
        changeLogService.log(request, "pump_station_technicals", result.id, "Yangi texnik ko'rsatkich qo'shildi", "CREATE")
        return result
    }

    @GetMapping("/technicals")
    fun getAllTechnicals(): List<PumpStationTechnicalResponseDto> = service.getAllTechnicals()

    @GetMapping("/technicals/{id}")
    fun getTechnicalById(@PathVariable id: Long): PumpStationTechnicalResponseDto = service.getTechnicalById(id)

    @PutMapping("/technicals/{id}")
    fun updateTechnical(
        request: HttpServletRequest,
        @PathVariable id: Long,
        @RequestBody dto: PumpStationTechnicalDto,
        @RequestParam(required = false, defaultValue = "") reason: String
    ): PumpStationTechnicalResponseDto {
        check(request)
        changeLogService.log(request, "pump_station_technicals", id, reason)
        return service.updateTechnical(id, dto)
    }

    @DeleteMapping("/technicals/{id}")
    fun deleteTechnical(request: HttpServletRequest, @PathVariable id: Long) {
        check(request)
        changeLogService.log(request, "pump_station_technicals", id, "Texnik ko'rsatkich o'chirildi", "DELETE")
        service.deleteTechnical(id)
    }
}
