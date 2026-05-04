package QishloqHojalik.Paxtachi.Services

import QishloqHojalik.Paxtachi.Entity.PaxtachiBalance
import QishloqHojalik.Paxtachi.Enums.Specialization
import QishloqHojalik.Paxtachi.Repositories.PaxtachiBalanceRepository
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.data.domain.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.Year


@Service
class PaxtachiBalanceService(
    private val repository: PaxtachiBalanceRepository
) {
    fun uploadExcel(file: MultipartFile): String {

        if (file.isEmpty) {
            throw RuntimeException("Excel fayl bo'sh")
        }
        val formatter = DataFormatter()
        val list = mutableListOf<PaxtachiBalance>()

        XSSFWorkbook(file.inputStream).use { workbook ->

            val sheet = workbook.getSheetAt(0)

            for (i in 6..sheet.lastRowNum) {

                val row = sheet.getRow(i) ?: continue

                val name = formatter.formatCellValue(row.getCell(1))

                if (name.isBlank()) continue

                val entity = PaxtachiBalance(

                    orderNumber = formatter.formatCellValue(row.getCell(0)).toIntOrNull(),

                    name = name,
                    farmType = formatter.formatCellValue(row.getCell(2)),
                    specialization = parseSpecialization(
                        formatter.formatCellValue(row.getCell(3))
                    ),
                    rightType = formatter.formatCellValue(row.getCell(4)),
                    stir = formatter.formatCellValue(row.getCell(5)),
                    cadastralNumber = formatter.formatCellValue(row.getCell(6)),

                    totalArea = formatter.formatCellValue(row.getCell(7)).replace(",", ".").toDoubleOrNull(),
                    irrigatedArea = formatter.formatCellValue(row.getCell(8)).replace(",", ".").toDoubleOrNull(),

                    cottonArea = formatter.formatCellValue(row.getCell(9)).replace(",", ".").toDoubleOrNull(),
                    wheatArea = formatter.formatCellValue(row.getCell(10)).replace(",", ".").toDoubleOrNull(),
                    greenhouseArea = formatter.formatCellValue(row.getCell(11)).replace(",", ".").toDoubleOrNull(),
                    lalmiArea = formatter.formatCellValue(row.getCell(12)).replace(",", ".").toDoubleOrNull(),

                    orchardArea = formatter.formatCellValue(row.getCell(13)).replace(",", ".").toDoubleOrNull(),
                    grapeArea = formatter.formatCellValue(row.getCell(14)).replace(",", ".").toDoubleOrNull(),
                    poplarArea = formatter.formatCellValue(row.getCell(15)).replace(",", ".").toDoubleOrNull(),
                    otherTreeArea = formatter.formatCellValue(row.getCell(16)).replace(",", ".").toDoubleOrNull(),

                    pastureArea = formatter.formatCellValue(row.getCell(17)).replace(",", ".").toDoubleOrNull(),
                    hayfieldArea = formatter.formatCellValue(row.getCell(18)).replace(",", ".").toDoubleOrNull(),

                    meliorationArea = formatter.formatCellValue(row.getCell(19)).replace(",", ".").toDoubleOrNull(),
                    pondArea = formatter.formatCellValue(row.getCell(20)).replace(",", ".").toDoubleOrNull(),
                    reservoirArea = formatter.formatCellValue(row.getCell(21)).replace(",", ".").toDoubleOrNull(),
                    canalArea = formatter.formatCellValue(row.getCell(22)).replace(",", ".").toDoubleOrNull(),

                    houseArea = formatter.formatCellValue(row.getCell(23)).replace(",", ".").toDoubleOrNull(),
                    yardArea = formatter.formatCellValue(row.getCell(24)).replace(",", ".").toDoubleOrNull(),

                    buildingArea = formatter.formatCellValue(row.getCell(25)).replace(",", ".").toDoubleOrNull(),
                    otherArea = formatter.formatCellValue(row.getCell(26)).replace(",", ".").toDoubleOrNull(),

                    totalBalance = formatter.formatCellValue(row.getCell(27)).replace(",", ".").toDoubleOrNull(),

                    season = Year.now().value
                )

                list.add(entity)
            }
        }

        repository.saveAll(list)

        return "Excel yuklandi (${list.size} ta qator)"
    }

    fun getFarmers(
        specializationId: String?,
        q: String?,
        page: Int?,
        pageSize: Int?,
        sort: String?,
        season: Int?
    ): Map<String, Any> {

        val currentSeason = season ?: Year.now().value
        val pageNumber = (page ?: 1) - 1
        val size = pageSize?.coerceIn(1, 100) ?: 20

        val pageable: Pageable = PageRequest.of(
            pageNumber,
            size,
            parseSort(sort)
        )

        val resultPage = when {

            // SEARCH
            !q.isNullOrBlank() ->
                repository.search(
                    q.lowercase(),
                    currentSeason,
                    pageable
                )

            // ENUM specialization
            !specializationId.isNullOrBlank() -> {

                val spec = Specialization.values()
                    .find { it.name.equals(specializationId, true) }

                if (spec != null) {
                    repository.findBySpecializationAndSeason(
                        spec,
                        currentSeason,
                        pageable
                    )
                } else {
                    repository.findBySeasonWithStir(
                        currentSeason,
                        pageable
                    )
                }
            }

            // DEFAULT
            else ->
                repository.findBySeasonWithStir(
                    currentSeason,
                    pageable
                )
        }

        return mapOf(
            "items" to resultPage.content,
            "page" to pageNumber + 1,
            "pageSize" to size,
            "total" to resultPage.totalElements
        )
    }

    fun getHududSection(name: String): List<PaxtachiBalance> {
        return repository.findHududSection(name, "Худуд жами")
    }

    private fun parseSort(sort: String?): Sort {

        if (sort.isNullOrBlank()) {
            return Sort.by(Sort.Direction.DESC, "totalArea")
        }

        val parts = sort.split(":")
        val field = parts[0]

        val mappedField = when (field) {
            "name" -> "name"
            "totalLandHa" -> "totalArea"
            "irrigatedHa" -> "irrigatedArea"
            "rainfedHa" -> "lalmiArea"
            else -> "totalArea"
        }

        val direction =
            if (parts.getOrNull(1) == "asc") Sort.Direction.ASC
            else Sort.Direction.DESC

        return Sort.by(direction, mappedField)
    }

    private fun toFarmerResponse(entity: PaxtachiBalance): Map<String, Any> {

        return mapOf(
            "id" to entity.id.toString(),
            "name" to (entity.name ?: ""),
            "stir" to normalizeStir(entity.stir),

            "specializations" to listOf(
                entity.specialization?.name ?: "OTHER"
            ),

            "totalLandHa" to (entity.totalArea ?: 0.0),
            "irrigatedHa" to (entity.irrigatedArea ?: 0.0),
            "rainfedHa" to (entity.lalmiArea ?: 0.0)
        )

    }

    private fun normalizeStir(stir: String?): String {

        if (stir.isNullOrBlank()) return "000000000"

        return stir.filter { it.isDigit() }
    }

    private fun normalizeSpecialization(raw: String?): List<String> {

        if (raw.isNullOrBlank()) {
            return listOf("OTHER")
        }

        val text = raw.lowercase()
        val result = mutableListOf<String>()

        if (text.contains("paxta")) result.add("PAHTA_GALLA")
        if (text.contains("baliq")) result.add("FISH")
        if (text.contains("bog")) result.add("ORCHARD")
        if (text.contains("uzum")) result.add("GRAPE")
        if (text.contains("sabzavot")) result.add("VEGETABLE")
        if (text.contains("issiqxona")) result.add("GREENHOUSE")
        if (text.contains("chorva")) result.add("LIVESTOCK")
        if (text.contains("parranda")) result.add("POULTRY")
        if (text.contains("asal")) result.add("BEEKEEPING")

        return if (result.isEmpty()) listOf("OTHER") else result
    }

    fun getFarmerAnalytics(
        id: Long,
        season: Int?
    ): Map<String, Any> {

        val currentSeason = season ?: Year.now().value

        val farmer = repository.findByIdAndSeason(
            id,
            currentSeason
        ) ?: throw RuntimeException("Fermer topilmadi")

        return mapOf(
            "farmer" to mapOf(
                "id" to farmer.id.toString(),
                "name" to (farmer.name ?: ""),
                "stir" to normalizeStir(farmer.stir),

                "specialization" to mapOf(
                    "code" to farmer.specialization?.name,
                    "nameUz" to farmer.specialization?.nameUz,
                    "icon" to farmer.specialization?.icon
                ),

                "farmType" to (farmer.farmType ?: ""),
                "rightType" to (farmer.rightType ?: "")
            ),

            "kpis" to mapOf(
                "totalLandHa" to (farmer.totalArea ?: 0.0),
                "irrigatedHa" to (farmer.irrigatedArea ?: 0.0),
                "rainfedHa" to (farmer.lalmiArea ?: 0.0)
            ),

            "landPie" to emptyList<Any>(),
            "parcels" to emptyList<Any>()
        )
    }

    fun getDashboardSummary(season: Int?): PaxtachiBalance {

        val currentSeason = season ?: Year.now().value

        return repository.findTumanJami(currentSeason)
            ?: throw RuntimeException("Туман жами topilmadi")
    }

    private fun parseSpecialization(text: String?): Specialization {

        if (text.isNullOrBlank()) return Specialization.OTHER

        val t = text.lowercase()

        return when {

            t.contains("пахта") || t.contains("paxta") ->
                Specialization.PAHTA_GALLA

            t.contains("боғ") || t.contains("bog") ->
                Specialization.ORCHARD

            t.contains("узум") || t.contains("uzum") ->
                Specialization.GRAPE

            t.contains("сабзавот") || t.contains("sabzavot") ->
                Specialization.VEGETABLE

            t.contains("иссикхона") || t.contains("issiqxona") ->
                Specialization.GREENHOUSE

            t.contains("чорва") || t.contains("chorva") ->
                Specialization.LIVESTOCK

            t.contains("парранда") || t.contains("parranda") ->
                Specialization.POULTRY

            t.contains("балиқ") || t.contains("baliq") ->
                Specialization.FISH

            t.contains("асал") || t.contains("asal") ->
                Specialization.BEEKEEPING

            t.contains("кўчат") || t.contains("kochat") ->
                Specialization.TREE_NURSERY


            else -> Specialization.OTHER
        }
    }
    fun getSpecializationSummary(season: Int?): Map<String, Any> {

        val currentSeason = season ?: Year.now().value

        val list = repository.findBySeason(
            currentSeason,
            Pageable.unpaged()
        ).content

        val grouped = list.groupBy {
            it.specialization ?: Specialization.OTHER
        }

        val items = grouped.map { (spec, farmers) ->

            val totalLand = farmers.sumOf { it.totalArea ?: 0.0 }

            mapOf(
                "id" to spec.name,
                "name" to spec.nameUz,
                "farmersCount" to farmers.size,
                "totalLandHa" to totalLand
            )
        }

        return mapOf(
            "items" to items
        )
    }
    private fun buildResponse(
        status: HttpStatus,
        message: String,
        code: String,
        details: List<Any>
    ): ResponseEntity<Map<String, Any>> {

        return ResponseEntity.status(status).body(
            mapOf(
                "message" to message,
                "code" to code,
                "details" to details
            )
        )
    }

}


