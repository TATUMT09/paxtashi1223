package QishloqHojalik.Paxtachi.Controllers

import QishloqHojalik.Paxtachi.Dtos.*
import QishloqHojalik.Paxtachi.Enums.Direction
import QishloqHojalik.Paxtachi.Security.AccessService
import QishloqHojalik.Paxtachi.Services.PumpExcelService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/pump")
class PumpExcelController(
    private val service: PumpExcelService,
    private val accessService: AccessService
) {

    private fun check(request: HttpServletRequest) {
        accessService.checkAccess(request, Direction.PUMP)
    }

    // EXCEL UPLOAD
    @PostMapping("/excel/import", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun importExcel(
        request: HttpServletRequest,
        @RequestParam("file") file: MultipartFile
    ): ExcelImportResponse {

        check(request)

        return service.importExcel(file)
    }

    // FARMER PUMP AGGREGATE CRUD
    @GetMapping("/farmer-pump-aggregates")
    fun getAllFarmers(
        request: HttpServletRequest
    ) : List<FarmerPumpAggregateResponseDto> {

        return service.getAllFarmers()
    }

    @GetMapping("/farmer-pump-aggregates/{id}")
    fun getFarmerById(
        request: HttpServletRequest,
        @PathVariable id: Long
    ): FarmerPumpAggregateResponseDto {

        return service.getFarmerById(id)
    }

    @PutMapping("/farmer-pump-aggregates/{id}")
    fun updateFarmer(
        request: HttpServletRequest,
        @PathVariable id: Long,
        @RequestBody dto: FarmerPumpAggregateDto
    ): FarmerPumpAggregateResponseDto {

        check(request)

        return service.updateFarmer(id, dto)
    }

    @DeleteMapping("/farmer-pump-aggregates/{id}")
    fun deleteFarmer(
        request: HttpServletRequest,
        @PathVariable id: Long
    ) {
        check(request)
        service.deleteFarmer(id)
    }

    // PUMP STATION CRUD
    @GetMapping("/stations")
    fun getAllStations(
        request: HttpServletRequest
    ): List<PumpStationResponseDto> {


        return service.getAllStations()
    }

    @GetMapping("/stations/{id}")
    fun getStationById(
        request: HttpServletRequest,
        @PathVariable id: Long
    ): PumpStationResponseDto {


        return service.getStationById(id)
    }

    @PutMapping("/stations/{id}")
    fun updateStation(
        request: HttpServletRequest,
        @PathVariable id: Long,
        @RequestBody dto: PumpStationDto
    ): PumpStationResponseDto {

        check(request)

        return service.updateStation(id, dto)
    }

    @DeleteMapping("/stations/{id}")
    fun deleteStation(
        request: HttpServletRequest,
        @PathVariable id: Long
    ) {
        check(request)
        service.deleteStation(id)
    }

    // PUMP STATION TECHNICAL CRUD
    @GetMapping("/technicals")
    fun getAllTechnicals(
        request: HttpServletRequest
    ): List<PumpStationTechnicalResponseDto> {

        return service.getAllTechnicals()
    }

    @GetMapping("/technicals/{id}")
    fun getTechnicalById(
        request: HttpServletRequest,
        @PathVariable id: Long
    ): PumpStationTechnicalResponseDto {


        return service.getTechnicalById(id)
    }

    @PutMapping("/technicals/{id}")
    fun updateTechnical(
        request: HttpServletRequest,
        @PathVariable id: Long,
        @RequestBody dto: PumpStationTechnicalDto
    ): PumpStationTechnicalResponseDto {


        return service.updateTechnical(id, dto)
    }

    @DeleteMapping("/technicals/{id}")
    fun deleteTechnical(
        request: HttpServletRequest,
        @PathVariable id: Long
    ) {
        check(request)
        service.deleteTechnical(id)
    }
}