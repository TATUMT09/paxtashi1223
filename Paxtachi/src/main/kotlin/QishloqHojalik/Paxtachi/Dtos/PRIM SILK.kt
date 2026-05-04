package QishloqHojalik.Paxtachi.Dtos


import QishloqHojalik.Paxtachi.Entity.GreenhouseRecord

data class GreenhouseRecordDto(
    val ownerName: String?,
    val totalArea: Double?,
    val contourNumber: String?,
    val greenhouseArea: Double?,
    val cropName: String?,
    val passportSeries: String?,
    val jshshir: String?,
    val mfy: String?,
    val seedlingCount: Int?,
    val requiredSeedlingCount: Int?,
    val locationUrl: String?,
    val phoneNumber: String?
)

data class GreenhouseRecordResponseDto(
    val id: Long,
    val ownerName: String?,
    val totalArea: Double?,
    val contourNumber: String?,
    val greenhouseArea: Double?,
    val cropName: String?,
    val passportSeries: String?,
    val jshshir: String?,
    val mfy: String?,
    val seedlingCount: Int?,
    val requiredSeedlingCount: Int?,
    val locationUrl: String?,
    val phoneNumber: String?
)

fun GreenhouseRecord.toResponseDto() = GreenhouseRecordResponseDto(
    id = this.id,
    ownerName = this.ownerName,
    totalArea = this.totalArea,
    contourNumber = this.contourNumber,
    greenhouseArea = this.greenhouseArea,
    cropName = this.cropName,
    passportSeries = this.passportSeries,
    jshshir = this.jshshir,
    mfy = this.mfy,
    seedlingCount = this.seedlingCount,
    requiredSeedlingCount = this.requiredSeedlingCount,
    locationUrl = this.locationUrl,
    phoneNumber = this.phoneNumber
)