package QishloqHojalik.Paxtachi.Dtos

import QishloqHojalik.Paxtachi.Entity.AgroTechnic

data class AgroTechnicDto(
    val ownerName: String?,
    val ownershipType: String?,
    val inn: String?,
    val district: String?,
    val workType: String?,
    val model: String?,
    val technicType: String?,
    val productionYear: Int?,
    val engineNumber: String?,
    val enginePower: Int?,
    val chassisNumber: String?,
    val color: String?,
    val regionCode: String?,
    val series: String?,
    val number: String?
)

data class AgroTechnicResponseDto(
    val id: Long,
    val ownerName: String?,
    val ownershipType: String?,
    val inn: String?,
    val district: String?,
    val workType: String?,
    val model: String?,
    val technicType: String?,
    val productionYear: Int?,
    val engineNumber: String?,
    val enginePower: Int?,
    val chassisNumber: String?,
    val color: String?,
    val regionCode: String?,
    val series: String?,
    val number: String?
)
fun AgroTechnic.toResponseDto() = AgroTechnicResponseDto(
    id = this.id,
    ownerName = this.ownerName,
    ownershipType = this.ownershipType,
    inn = this.inn,
    district = this.district,
    workType = this.workType,
    model = this.model,
    technicType = this.technicType,
    productionYear = this.productionYear,
    engineNumber = this.engineNumber,
    enginePower = this.enginePower,
    chassisNumber = this.chassisNumber,
    color = this.color,
    regionCode = this.regionCode,
    series = this.series,
    number = this.number
)