package QishloqHojalik.Paxtachi.Entity

import jakarta.persistence.Entity
import jakarta.persistence.Table


@Entity
@Table(name = "land_contours")
class LandContour(

    var contourNumber: String? = null,
    var totalArea: Double? = null,

    var irrigatedAgricultureArea: Double? = null,
    var irrigatedTomorqaArea: Double? = null,

    var orchardArea: Double? = null,
    var vineyardArea: Double? = null,
    var mulberryArea: Double? = null,
    var nurseryArea: Double? = null,
    var meliorationArea: Double? = null,

    var grayLandAgricultureArea: Double? = null,
    var grayLandPastureArea: Double? = null,

    var commonYardArea: Double? = null,

    var agricultureTotalArea: Double? = null,
    var agricultureArableArea: Double? = null,
    var agricultureIrrigatedArea: Double? = null,

    var tomorqaTotalArea: Double? = null,
    var tomorqaInsideSettlementArea: Double? = null,

    var treePlantationArea: Double? = null,
    var pastureArea: Double? = null,
    var lakeFishArea: Double? = null,
    var publicBuildingsArea: Double? = null,
    var streetsArea: Double? = null,
    var cemeteryArea: Double? = null,
    var constructionArea: Double? = null,
    var forestArea: Double? = null,
    var canalsArea: Double? = null,
    var roadsArea: Double? = null,
    var otherLandsArea: Double? = null,
    var buildingUnderArea: Double? = null,
    var agriculturalServiceArea: Double? = null,
    var cottonReceptionArea: Double? = null,
    var abandonedFarmBuildingArea: Double? = null,
    var auxiliaryFarmArea: Double? = null

) : BaseEntity()