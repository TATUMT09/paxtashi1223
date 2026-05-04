package QishloqHojalik.Paxtachi.Dtos


import QishloqHojalik.Paxtachi.Entity.LandContour

data class ExcelImportResponse(
    val farmerPumpAggregateSaved: Int = 0,
    val pumpStationSaved: Int = 0,
    val pumpStationTechnicalSaved: Int = 0,
    val cadastreSaved: Int = 0,
    val landContourSaved: Int = 0,
    val agroTechnicSaved: Int = 0,
    val farmRepresentativeSaved: Int = 0,
    val greenhouseSaved: Int = 0,
    val errors: List<String> = emptyList()
)

data class LandContourDto(
    val contourNumber: String?,
    val totalArea: Double?,

    val irrigatedAgricultureArea: Double?,
    val irrigatedTomorqaArea: Double?,

    val orchardArea: Double?,
    val vineyardArea: Double?,
    val mulberryArea: Double?,
    val nurseryArea: Double?,
    val meliorationArea: Double?,

    val grayLandAgricultureArea: Double?,
    val grayLandPastureArea: Double?,

    val commonYardArea: Double?,

    val agricultureTotalArea: Double?,
    val agricultureArableArea: Double?,
    val agricultureIrrigatedArea: Double?,

    val tomorqaTotalArea: Double?,
    val tomorqaInsideSettlementArea: Double?,

    val treePlantationArea: Double?,
    val pastureArea: Double?,
    val lakeFishArea: Double?,
    val publicBuildingsArea: Double?,
    val streetsArea: Double?,
    val cemeteryArea: Double?,
    val constructionArea: Double?,
    val forestArea: Double?,
    val canalsArea: Double?,
    val roadsArea: Double?,
    val otherLandsArea: Double?,
    val buildingUnderArea: Double?,
    val agriculturalServiceArea: Double?,
    val cottonReceptionArea: Double?,
    val abandonedFarmBuildingArea: Double?,
    val auxiliaryFarmArea: Double?
)

data class LandContourResponseDto(
    val id: Long,
    val contourNumber: String?,
    val totalArea: Double?,

    val irrigatedAgricultureArea: Double?,
    val irrigatedTomorqaArea: Double?,

    val orchardArea: Double?,
    val vineyardArea: Double?,
    val mulberryArea: Double?,
    val nurseryArea: Double?,
    val meliorationArea: Double?,

    val grayLandAgricultureArea: Double?,
    val grayLandPastureArea: Double?,

    val commonYardArea: Double?,

    val agricultureTotalArea: Double?,
    val agricultureArableArea: Double?,
    val agricultureIrrigatedArea: Double?,

    val tomorqaTotalArea: Double?,
    val tomorqaInsideSettlementArea: Double?,

    val treePlantationArea: Double?,
    val pastureArea: Double?,
    val lakeFishArea: Double?,
    val publicBuildingsArea: Double?,
    val streetsArea: Double?,
    val cemeteryArea: Double?,
    val constructionArea: Double?,
    val forestArea: Double?,
    val canalsArea: Double?,
    val roadsArea: Double?,
    val otherLandsArea: Double?,
    val buildingUnderArea: Double?,
    val agriculturalServiceArea: Double?,
    val cottonReceptionArea: Double?,
    val abandonedFarmBuildingArea: Double?,
    val auxiliaryFarmArea: Double?
)

fun LandContour.toResponseDto() = LandContourResponseDto(
    id = this.id,
    contourNumber = this.contourNumber,
    totalArea = this.totalArea,
    irrigatedAgricultureArea = this.irrigatedAgricultureArea,
    irrigatedTomorqaArea = this.irrigatedTomorqaArea,
    orchardArea = this.orchardArea,
    vineyardArea = this.vineyardArea,
    mulberryArea = this.mulberryArea,
    nurseryArea = this.nurseryArea,
    meliorationArea = this.meliorationArea,
    grayLandAgricultureArea = this.grayLandAgricultureArea,
    grayLandPastureArea = this.grayLandPastureArea,
    commonYardArea = this.commonYardArea,
    agricultureTotalArea = this.agricultureTotalArea,
    agricultureArableArea = this.agricultureArableArea,
    agricultureIrrigatedArea = this.agricultureIrrigatedArea,
    tomorqaTotalArea = this.tomorqaTotalArea,
    tomorqaInsideSettlementArea = this.tomorqaInsideSettlementArea,
    treePlantationArea = this.treePlantationArea,
    pastureArea = this.pastureArea,
    lakeFishArea = this.lakeFishArea,
    publicBuildingsArea = this.publicBuildingsArea,
    streetsArea = this.streetsArea,
    cemeteryArea = this.cemeteryArea,
    constructionArea = this.constructionArea,
    forestArea = this.forestArea,
    canalsArea = this.canalsArea,
    roadsArea = this.roadsArea,
    otherLandsArea = this.otherLandsArea,
    buildingUnderArea = this.buildingUnderArea,
    agriculturalServiceArea = this.agriculturalServiceArea,
    cottonReceptionArea = this.cottonReceptionArea,
    abandonedFarmBuildingArea = this.abandonedFarmBuildingArea,
    auxiliaryFarmArea = this.auxiliaryFarmArea
)