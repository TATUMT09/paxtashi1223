package QishloqHojalik.Paxtachi.Dtos

import QishloqHojalik.Paxtachi.Entity.CadastreLandRecord

data class CadastreLandRecordDto(
    val regionName: String?,
    val districtName: String?,
    val neighborhoodName: String?,

    val contourNumber: String?,
    val area: Double?,

    val landType: String?,
    val farmName: String?,

    val landNumber: String?,
    val cadastreNumber: String?,

    val cadastralRegistered: Int?,
    val notRegisteredReason: String?,

    val problem: String?,
    val problemDescription: String?
)

data class CadastreLandRecordResponseDto(
    val id: Long,

    val regionName: String?,
    val districtName: String?,
    val neighborhoodName: String?,

    val contourNumber: String?,
    val area: Double?,

    val landType: String?,
    val farmName: String?,

    val landNumber: String?,
    val cadastreNumber: String?,

    val cadastralRegistered: Int?,
    val notRegisteredReason: String?,

    val problem: String?,
    val problemDescription: String?
)

fun CadastreLandRecord.toResponseDto() = CadastreLandRecordResponseDto(
    id = this.id,
    regionName = this.regionName,
    districtName = this.districtName,
    neighborhoodName = this.neighborhoodName,
    contourNumber = this.contourNumber,
    area = this.area,
    landType = this.landType,
    farmName = this.farmName,
    landNumber = this.landNumber,
    cadastreNumber = this.cadastreNumber,
    cadastralRegistered = this.cadastralRegistered,
    notRegisteredReason = this.notRegisteredReason,
    problem = this.problem,
    problemDescription = this.problemDescription
)
