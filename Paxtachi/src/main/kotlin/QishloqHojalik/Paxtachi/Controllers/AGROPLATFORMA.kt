package QishloqHojalik.Paxtachi.Controllers


import QishloqHojalik.Paxtachi.Dtos.*
import QishloqHojalik.Paxtachi.Services.PumpExcelService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/pump")
class PumpExcelController(
    private val service: PumpExcelService
) {

    // EXCEL UPLOAD
    @PostMapping("/excel/import", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun importExcel(
        @RequestParam("file") file: MultipartFile
    ): ExcelImportResponse {
        return service.importExcel(file)
    }

    // FARMER PUMP AGGREGATE CRUD
    @GetMapping("/farmer-pump-aggregates")
    fun getAllFarmers() = service.getAllFarmers()

    @GetMapping("/farmer-pump-aggregates/{id}")
    fun getFarmerById(@PathVariable id: Long) =
        service.getFarmerById(id)

    @PutMapping("/farmer-pump-aggregates/{id}")
    fun updateFarmer(
        @PathVariable id: Long,
        @RequestBody dto: FarmerPumpAggregateDto
    ) = service.updateFarmer(id, dto)

    @DeleteMapping("/farmer-pump-aggregates/{id}")
    fun deleteFarmer(@PathVariable id: Long) =
        service.deleteFarmer(id)


    // PUMP STATION CRUD
    @GetMapping("/stations")
    fun getAllStations() = service.getAllStations()

    @GetMapping("/stations/{id}")
    fun getStationById(@PathVariable id: Long) =
        service.getStationById(id)

    @PutMapping("/stations/{id}")
    fun updateStation(
        @PathVariable id: Long,
        @RequestBody dto: PumpStationDto
    ) = service.updateStation(id, dto)

    @DeleteMapping("/stations/{id}")
    fun deleteStation(@PathVariable id: Long) =
        service.deleteStation(id)


    // PUMP STATION TECHNICAL CRUD
    @GetMapping("/technicals")
    fun getAllTechnicals() = service.getAllTechnicals()

    @GetMapping("/technicals/{id}")
    fun getTechnicalById(@PathVariable id: Long) =
        service.getTechnicalById(id)

    @PutMapping("/technicals/{id}")
    fun updateTechnical(
        @PathVariable id: Long,
        @RequestBody dto: PumpStationTechnicalDto
    ) = service.updateTechnical(id, dto)

    @DeleteMapping("/technicals/{id}")
    fun deleteTechnical(@PathVariable id: Long) =
        service.deleteTechnical(id)
}