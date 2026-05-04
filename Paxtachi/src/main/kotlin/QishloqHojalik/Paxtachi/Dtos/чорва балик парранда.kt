package QishloqHojalik.Paxtachi.Dtos


import QishloqHojalik.Paxtachi.Entity.FarmRepresentative

data class FarmRepresentativeDto(
    val inn: String?,
    val farmName: String?,
    val activityType: String?,
    val farmerFullName: String?,
    val farmerPhone: String?,
    val livestockCount: Double?,
    val landManagerFullName: String?,
    val landManagerPhone: String?
)

data class FarmRepresentativeResponseDto(
    val id: Long,
    val inn: String?,
    val farmName: String?,
    val activityType: String?,
    val farmerFullName: String?,
    val farmerPhone: String?,
    val livestockCount: Double?,
    val landManagerFullName: String?,
    val landManagerPhone: String?
)

fun FarmRepresentative.toResponseDto() = FarmRepresentativeResponseDto(
    id = this.id,
    inn = this.inn,
    farmName = this.farmName,
    activityType = this.activityType,
    farmerFullName = this.farmerFullName,
    farmerPhone = this.farmerPhone,
    livestockCount = this.livestockCount,
    landManagerFullName = this.landManagerFullName,
    landManagerPhone = this.landManagerPhone
)