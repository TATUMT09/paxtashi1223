package QishloqHojalik.Paxtachi.Dtos

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
