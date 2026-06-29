package QishloqHojalik.Paxtachi.Dtos

data class FarmerUpdateDto(
    val name: String? = null,
    val stir: String? = null,
    val farmType: String? = null,
    val specialization: String? = null,
    val rightType: String? = null,
    val cadastralNumber: String? = null,
    val totalArea: Double? = null,
    val irrigatedArea: Double? = null,
    val cottonArea: Double? = null,
    val wheatArea: Double? = null,
    val greenhouseArea: Double? = null,
    val lalmiArea: Double? = null,
    val orchardArea: Double? = null,
    val grapeArea: Double? = null,
    val poplarArea: Double? = null,
    val otherTreeArea: Double? = null,
    val pastureArea: Double? = null,
    val hayfieldArea: Double? = null,
    val meliorationArea: Double? = null,
    val pondArea: Double? = null,
    val reservoirArea: Double? = null,
    val canalArea: Double? = null,
    val houseArea: Double? = null,
    val yardArea: Double? = null,
    val buildingArea: Double? = null,
    val otherArea: Double? = null,
    val totalBalance: Double? = null
)

data class SpecializationSummaryDto(
    val id: Long,
    val count: Long
)
data class LandSummaryDto(
    val totalArea: Double,
    val irrigatedArea: Double,
    val nonIrrigatedArea: Double
)
data class LandItemsSummaryDto(
    val cotton: Double,
    val wheat: Double,
    val vegetable: Double,
    val orchard: Double,
    val vineyard: Double,
    val pasture: Double
)
