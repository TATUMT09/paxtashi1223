package QishloqHojalik.Paxtachi.Controllers

import QishloqHojalik.Paxtachi.Repositories.*
import jakarta.servlet.http.HttpServletRequest
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/api/export")
class ExcelExportController(
    private val agroTechnicRepo: AgroTechnicRepository,
    private val cadastreRepo: CadastreLandRecordRepository,
    private val greenhouseRepo: GreenhouseRecordRepository,
    private val farmRepRepo: FarmRepresentativeRepository,
    private val landContourRepo: LandContourRepository,
    private val farmerPumpRepo: FarmerPumpAggregateRepository,
    private val pumpStationRepo: PumpStationRepository,
    private val pumpTechnicalRepo: PumpStationTechnicalRepository,
    private val farmerRepo: PaxtachiBalanceRepository
) {

    private val dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    private val fileFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")

    @GetMapping("/excel")
    fun exportExcel(request: HttpServletRequest): ResponseEntity<ByteArray> {
        val direction = request.getAttribute("direction") as? String ?: "UNKNOWN"
        val now = LocalDateTime.now()
        val nowStr = now.format(dateFmt)
        val fileDate = now.format(fileFmt)

        val workbook = XSSFWorkbook()
        val titleStyle = workbook.createCellStyle().apply {
            fillForegroundColor = IndexedColors.LIGHT_GREEN.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            setFont(workbook.createFont().apply {
                bold = true
                fontHeightInPoints = 11
            })
        }
        val headerStyle = workbook.createCellStyle().apply {
            fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            setFont(workbook.createFont().apply { bold = true })
        }

        fun Sheet.addTitle(text: String) {
            val row = createRow(0)
            val cell = row.createCell(0)
            cell.setCellValue("Shu kungi hisobot: $text | Yuklab olingan vaqt: $nowStr")
            cell.cellStyle = titleStyle
        }

        fun Sheet.addHeaders(headers: List<String>, rowNum: Int = 1) {
            val row = createRow(rowNum)
            headers.forEachIndexed { i, h ->
                val cell = row.createCell(i)
                cell.setCellValue(h)
                cell.cellStyle = headerStyle
            }
        }

        when (direction) {
            "AGRO_TECHNIC" -> {
                val sheet = workbook.createSheet("Fermer Texnikasi")
                sheet.addTitle("Fermer Texnikasi")
                sheet.addHeaders(listOf("№", "Egasi", "Mulkchilik turi", "INN", "Tuman", "Ish turi", "Model", "Texnika turi", "Ishlab chiqarilgan yil", "Dvigatel №", "Quvvat (ot)", "Shassi №", "Rang", "Viloyat kodi", "Seriya", "Raqam"))
                agroTechnicRepo.findAllByDeletedFalse().forEachIndexed { idx, r ->
                    val row = sheet.createRow(idx + 2)
                    row.createCell(0).setCellValue((idx + 1).toDouble())
                    row.createCell(1).setCellValue(r.ownerName ?: "")
                    row.createCell(2).setCellValue(r.ownershipType ?: "")
                    row.createCell(3).setCellValue(r.inn ?: "")
                    row.createCell(4).setCellValue(r.district ?: "")
                    row.createCell(5).setCellValue(r.workType ?: "")
                    row.createCell(6).setCellValue(r.model ?: "")
                    row.createCell(7).setCellValue(r.technicType ?: "")
                    row.createCell(8).setCellValue(r.productionYear?.toDouble() ?: 0.0)
                    row.createCell(9).setCellValue(r.engineNumber ?: "")
                    row.createCell(10).setCellValue(r.enginePower?.toDouble() ?: 0.0)
                    row.createCell(11).setCellValue(r.chassisNumber ?: "")
                    row.createCell(12).setCellValue(r.color ?: "")
                    row.createCell(13).setCellValue(r.regionCode ?: "")
                    row.createCell(14).setCellValue(r.series ?: "")
                    row.createCell(15).setCellValue(r.number ?: "")
                }
            }

            "CADASTRE" -> {
                val sheet = workbook.createSheet("Kadastr Yozuvlari")
                sheet.addTitle("Kadastr Yozuvlari")
                sheet.addHeaders(listOf("№", "Viloyat", "Tuman", "Mahalla", "Kontur raqami", "Maydoni (ga)", "Yer turi", "Xo'jalik nomi", "Yer raqami", "Kadastr raqami", "Kadastrdan o'tgan", "Sabablar", "Muammo", "Muammo sababi"))
                cadastreRepo.findAllByDeletedFalse().forEachIndexed { idx, r ->
                    val row = sheet.createRow(idx + 2)
                    row.createCell(0).setCellValue((idx + 1).toDouble())
                    row.createCell(1).setCellValue(r.regionName ?: "")
                    row.createCell(2).setCellValue(r.districtName ?: "")
                    row.createCell(3).setCellValue(r.neighborhoodName ?: "")
                    row.createCell(4).setCellValue(r.contourNumber ?: "")
                    row.createCell(5).setCellValue(r.area ?: 0.0)
                    row.createCell(6).setCellValue(r.landType ?: "")
                    row.createCell(7).setCellValue(r.farmName ?: "")
                    row.createCell(8).setCellValue(r.landNumber ?: "")
                    row.createCell(9).setCellValue(r.cadastreNumber ?: "")
                    row.createCell(10).setCellValue(r.cadastralRegistered?.toDouble() ?: 0.0)
                    row.createCell(11).setCellValue(r.notRegisteredReason ?: "")
                    row.createCell(12).setCellValue(r.problem ?: "")
                    row.createCell(13).setCellValue(r.problemDescription ?: "")
                }
            }

            "GREENHOUSE" -> {
                val sheet = workbook.createSheet("Issiqxonalar")
                sheet.addTitle("Issiqxonalar")
                sheet.addHeaders(listOf("№", "Egasi", "Umumiy maydon", "Kontur raqami", "Issiqxona maydoni", "Ekin nomi", "Pasport seriyasi", "JShShIR", "MFY", "Ko'chat soni", "Kerakli ko'chat", "Joylashuv", "Telefon"))
                greenhouseRepo.findAllByDeletedFalse().forEachIndexed { idx, r ->
                    val row = sheet.createRow(idx + 2)
                    row.createCell(0).setCellValue((idx + 1).toDouble())
                    row.createCell(1).setCellValue(r.ownerName ?: "")
                    row.createCell(2).setCellValue(r.totalArea ?: 0.0)
                    row.createCell(3).setCellValue(r.contourNumber ?: "")
                    row.createCell(4).setCellValue(r.greenhouseArea ?: 0.0)
                    row.createCell(5).setCellValue(r.cropName ?: "")
                    row.createCell(6).setCellValue(r.passportSeries ?: "")
                    row.createCell(7).setCellValue(r.jshshir ?: "")
                    row.createCell(8).setCellValue(r.mfy ?: "")
                    row.createCell(9).setCellValue(r.seedlingCount?.toDouble() ?: 0.0)
                    row.createCell(10).setCellValue(r.requiredSeedlingCount?.toDouble() ?: 0.0)
                    row.createCell(11).setCellValue(r.locationUrl ?: "")
                    row.createCell(12).setCellValue(r.phoneNumber ?: "")
                }
            }

            "FARM_REPRESENTATIVE" -> {
                val sheet = workbook.createSheet("Fermer Vakillari")
                sheet.addTitle("Fermer Vakillari")
                sheet.addHeaders(listOf("№", "INN", "Xo'jalik nomi", "Faoliyat turi", "Fermer F.I.O", "Fermer tel.", "Chorva soni", "Yer boshqaruvchi F.I.O", "Yer boshqaruvchi tel."))
                farmRepRepo.findAllByDeletedFalse().forEachIndexed { idx, r ->
                    val row = sheet.createRow(idx + 2)
                    row.createCell(0).setCellValue((idx + 1).toDouble())
                    row.createCell(1).setCellValue(r.inn ?: "")
                    row.createCell(2).setCellValue(r.farmName ?: "")
                    row.createCell(3).setCellValue(r.activityType ?: "")
                    row.createCell(4).setCellValue(r.farmerFullName ?: "")
                    row.createCell(5).setCellValue(r.farmerPhone ?: "")
                    row.createCell(6).setCellValue(r.livestockCount ?: 0.0)
                    row.createCell(7).setCellValue(r.landManagerFullName ?: "")
                    row.createCell(8).setCellValue(r.landManagerPhone ?: "")
                }
            }

            "LAND_CONTOUR" -> {
                val sheet = workbook.createSheet("Yer Konturlari")
                sheet.addTitle("Yer Konturlari")
                sheet.addHeaders(listOf("№", "Kontur raqami", "Umumiy maydon", "Sug'oriladigan qishloq x.", "Sug'oriladigan tomorqa", "Bog'", "Uzumzor", "Tut", "Ko'chatchilik", "Melioratsiya", "Sug'orilmaydigan qishloq x.", "Sug'orilmaydigan yaylov", "Umumiy hovli", "Qishloq x. jami", "Ekin maydoni", "Sug'oriladigan", "Tomorqa jami", "Aholi punktida", "Daraxt plantatsiyasi", "Yaylov", "Ko'l, baliqchilik", "Jamoat binoalari", "Ko'cha", "Qabriston", "Qurilish", "O'rmon", "Ariqlar", "Yo'llar", "Boshqa yerlar", "Bino ostidagi", "Qishloq x. xizmati", "Paxta qabul punkt", "Tashlandiq", "Yordamchi xo'jalik"))
                landContourRepo.findAllByDeletedFalse().forEachIndexed { idx, r ->
                    val row = sheet.createRow(idx + 2)
                    row.createCell(0).setCellValue((idx + 1).toDouble())
                    row.createCell(1).setCellValue(r.contourNumber ?: "")
                    row.createCell(2).setCellValue(r.totalArea ?: 0.0)
                    row.createCell(3).setCellValue(r.irrigatedAgricultureArea ?: 0.0)
                    row.createCell(4).setCellValue(r.irrigatedTomorqaArea ?: 0.0)
                    row.createCell(5).setCellValue(r.orchardArea ?: 0.0)
                    row.createCell(6).setCellValue(r.vineyardArea ?: 0.0)
                    row.createCell(7).setCellValue(r.mulberryArea ?: 0.0)
                    row.createCell(8).setCellValue(r.nurseryArea ?: 0.0)
                    row.createCell(9).setCellValue(r.meliorationArea ?: 0.0)
                    row.createCell(10).setCellValue(r.grayLandAgricultureArea ?: 0.0)
                    row.createCell(11).setCellValue(r.grayLandPastureArea ?: 0.0)
                    row.createCell(12).setCellValue(r.commonYardArea ?: 0.0)
                    row.createCell(13).setCellValue(r.agricultureTotalArea ?: 0.0)
                    row.createCell(14).setCellValue(r.agricultureArableArea ?: 0.0)
                    row.createCell(15).setCellValue(r.agricultureIrrigatedArea ?: 0.0)
                    row.createCell(16).setCellValue(r.tomorqaTotalArea ?: 0.0)
                    row.createCell(17).setCellValue(r.tomorqaInsideSettlementArea ?: 0.0)
                    row.createCell(18).setCellValue(r.treePlantationArea ?: 0.0)
                    row.createCell(19).setCellValue(r.pastureArea ?: 0.0)
                    row.createCell(20).setCellValue(r.lakeFishArea ?: 0.0)
                    row.createCell(21).setCellValue(r.publicBuildingsArea ?: 0.0)
                    row.createCell(22).setCellValue(r.streetsArea ?: 0.0)
                    row.createCell(23).setCellValue(r.cemeteryArea ?: 0.0)
                    row.createCell(24).setCellValue(r.constructionArea ?: 0.0)
                    row.createCell(25).setCellValue(r.forestArea ?: 0.0)
                    row.createCell(26).setCellValue(r.canalsArea ?: 0.0)
                    row.createCell(27).setCellValue(r.roadsArea ?: 0.0)
                    row.createCell(28).setCellValue(r.otherLandsArea ?: 0.0)
                    row.createCell(29).setCellValue(r.buildingUnderArea ?: 0.0)
                    row.createCell(30).setCellValue(r.agriculturalServiceArea ?: 0.0)
                    row.createCell(31).setCellValue(r.cottonReceptionArea ?: 0.0)
                    row.createCell(32).setCellValue(r.abandonedFarmBuildingArea ?: 0.0)
                    row.createCell(33).setCellValue(r.auxiliaryFarmArea ?: 0.0)
                }
            }

            "PUMP" -> {
                // Sheet 1: Fermerlar
                val s1 = workbook.createSheet("Fermerlar")
                s1.addTitle("Nasos Fermerlar")
                s1.addHeaders(listOf("№", "Fermer nomi", "INN", "Manzil", "Nasos modeli", "Ishlab chiqarilgan yil", "Suv ko'tarish balandligi", "Suv sarfi", "Dvigatel quvvati", "Suv manbai", "Ulangan maydon (ga)"))
                farmerPumpRepo.findAllByDeletedFalse().forEachIndexed { idx, r ->
                    val row = s1.createRow(idx + 2)
                    row.createCell(0).setCellValue((idx + 1).toDouble())
                    row.createCell(1).setCellValue(r.farmerName ?: "")
                    row.createCell(2).setCellValue(r.inn ?: "")
                    row.createCell(3).setCellValue(r.address ?: "")
                    row.createCell(4).setCellValue(r.pumpModel ?: "")
                    row.createCell(5).setCellValue(r.productionYear?.toDouble() ?: 0.0)
                    row.createCell(6).setCellValue(r.waterLiftHeight ?: 0.0)
                    row.createCell(7).setCellValue(r.waterFlow ?: 0.0)
                    row.createCell(8).setCellValue(r.enginePower ?: "")
                    row.createCell(9).setCellValue(r.waterSource ?: "")
                    row.createCell(10).setCellValue(r.connectedArea ?: 0.0)
                }

                // Sheet 2: Stansiyalar
                val s2 = workbook.createSheet("Stansiyalar")
                s2.addTitle("Nasos Stansiyalari")
                s2.addHeaders(listOf("№", "Stansiya nomi", "O'rnatilgan agregatlar", "Ishlaydigan agregatlar", "Suv manbai"))
                pumpStationRepo.findAllByDeletedFalse().forEachIndexed { idx, r ->
                    val row = s2.createRow(idx + 2)
                    row.createCell(0).setCellValue((idx + 1).toDouble())
                    row.createCell(1).setCellValue(r.stationName ?: "")
                    row.createCell(2).setCellValue(r.installedAggregateCount?.toDouble() ?: 0.0)
                    row.createCell(3).setCellValue(r.workingAggregateCount?.toDouble() ?: 0.0)
                    row.createCell(4).setCellValue(r.waterSource ?: "")
                }

                // Sheet 3: Texnik ko'rsatkichlar
                val s3 = workbook.createSheet("Texnik")
                s3.addTitle("Texnik Ko'rsatkichlar")
                s3.addHeaders(listOf("№", "Nasos modeli", "Elektr dvigatel modeli", "Suv sarfi", "Quvvat (kVt)", "Aylanish tezligi", "Ko'tarish balandligi", "Foydalanishga topshirilgan yil", "Ulangan maydon", "Balans qiymati", "Bosim trubka diametri", "Bosim trubka uzunligi", "Jami uzunlik", "Foydalanuvchi fermerlar", "INN", "Stansiya"))
                pumpTechnicalRepo.findAllByDeletedFalse().forEachIndexed { idx, r ->
                    val row = s3.createRow(idx + 2)
                    row.createCell(0).setCellValue((idx + 1).toDouble())
                    row.createCell(1).setCellValue(r.pumpModel ?: "")
                    row.createCell(2).setCellValue(r.electricEngineModel ?: "")
                    row.createCell(3).setCellValue(r.waterFlow ?: 0.0)
                    row.createCell(4).setCellValue(r.enginePowerKw ?: 0.0)
                    row.createCell(5).setCellValue(r.engineRotation ?: 0.0)
                    row.createCell(6).setCellValue(r.waterLiftHeight ?: 0.0)
                    row.createCell(7).setCellValue(r.commissioningYear?.toDouble() ?: 0.0)
                    row.createCell(8).setCellValue(r.attachedArea ?: 0.0)
                    row.createCell(9).setCellValue(r.balanceValue ?: 0.0)
                    row.createCell(10).setCellValue(r.pressurePipeDiameter ?: 0.0)
                    row.createCell(11).setCellValue(r.pressurePipeLength ?: 0.0)
                    row.createCell(12).setCellValue(r.pressurePipeTotalLength ?: 0.0)
                    row.createCell(13).setCellValue(r.usedFarmers ?: "")
                    row.createCell(14).setCellValue(r.inn ?: "")
                    row.createCell(15).setCellValue(r.pumpStation?.stationName ?: "")
                }
            }

            else -> {
                // PAXTACHILIK, TUTCHILIK, GALLACHILIK, BOGDORCHILIK, CHORVACHILIK, SUPER_ADMIN, etc.
                val sheet = workbook.createSheet("Fermerlar")
                sheet.addTitle("Fermerlar ro'yxati ($direction)")
                sheet.addHeaders(listOf("№", "Nomi", "Xo'jalik turi", "Mutaxassislik", "Haq turi", "STIR", "Kadastr raqami", "Umumiy maydon", "Sug'oriladigan", "Paxta", "G'alla", "Issiqxona", "Lalmi", "Bog'", "Uzum", "Terak", "Boshqa daraxt", "Yaylov", "O'tloq", "Melioratsiya", "Ko'l", "Suv ombori", "Ariq", "Uy", "Hovli", "Qurilish", "Boshqa", "Jami balans", "Yil"))
                val currentYear = java.time.Year.now().value
                farmerRepo.findBySeason(currentYear, org.springframework.data.domain.PageRequest.of(0, 10000)).content.forEachIndexed { idx, r ->
                    val row = sheet.createRow(idx + 2)
                    row.createCell(0).setCellValue((idx + 1).toDouble())
                    row.createCell(1).setCellValue(r.name ?: "")
                    row.createCell(2).setCellValue(r.farmType ?: "")
                    row.createCell(3).setCellValue(r.specialization?.nameUz ?: "")
                    row.createCell(4).setCellValue(r.rightType ?: "")
                    row.createCell(5).setCellValue(r.stir ?: "")
                    row.createCell(6).setCellValue(r.cadastralNumber ?: "")
                    row.createCell(7).setCellValue(r.totalArea ?: 0.0)
                    row.createCell(8).setCellValue(r.irrigatedArea ?: 0.0)
                    row.createCell(9).setCellValue(r.cottonArea ?: 0.0)
                    row.createCell(10).setCellValue(r.wheatArea ?: 0.0)
                    row.createCell(11).setCellValue(r.greenhouseArea ?: 0.0)
                    row.createCell(12).setCellValue(r.lalmiArea ?: 0.0)
                    row.createCell(13).setCellValue(r.orchardArea ?: 0.0)
                    row.createCell(14).setCellValue(r.grapeArea ?: 0.0)
                    row.createCell(15).setCellValue(r.poplarArea ?: 0.0)
                    row.createCell(16).setCellValue(r.otherTreeArea ?: 0.0)
                    row.createCell(17).setCellValue(r.pastureArea ?: 0.0)
                    row.createCell(18).setCellValue(r.hayfieldArea ?: 0.0)
                    row.createCell(19).setCellValue(r.meliorationArea ?: 0.0)
                    row.createCell(20).setCellValue(r.pondArea ?: 0.0)
                    row.createCell(21).setCellValue(r.reservoirArea ?: 0.0)
                    row.createCell(22).setCellValue(r.canalArea ?: 0.0)
                    row.createCell(23).setCellValue(r.houseArea ?: 0.0)
                    row.createCell(24).setCellValue(r.yardArea ?: 0.0)
                    row.createCell(25).setCellValue(r.buildingArea ?: 0.0)
                    row.createCell(26).setCellValue(r.otherArea ?: 0.0)
                    row.createCell(27).setCellValue(r.totalBalance ?: 0.0)
                    row.createCell(28).setCellValue(r.season?.toDouble() ?: 0.0)
                }
            }
        }

        val out = ByteArrayOutputStream()
        workbook.write(out)
        workbook.close()

        val fileName = "hisobot_${direction.lowercase()}_$fileDate.xlsx"
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$fileName\"")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(out.toByteArray())
    }
}
