package QishloqHojalik.Paxtachi.Entity


import jakarta.persistence.*

@Entity
@Table(name = "farm_representatives")
class FarmRepresentative(

    var inn: String? = null,

    @Column(columnDefinition = "TEXT")
    var farmName: String? = null,

    var activityType: String? = null,

    var farmerFullName: String? = null,
    var farmerPhone: String? = null,

    var livestockCount: Double? = null,

    var landManagerFullName: String? = null,
    var landManagerPhone: String? = null

) : BaseEntity()