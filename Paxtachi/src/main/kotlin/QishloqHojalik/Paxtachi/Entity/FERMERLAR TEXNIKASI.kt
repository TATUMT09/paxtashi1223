package QishloqHojalik.Paxtachi.Entity


import jakarta.persistence.*

@Entity
@Table(name = "agro_technics")
class AgroTechnic(

    var ownerName: String? = null,
    var ownershipType: String? = null,
    var inn: String? = null,
    var district: String? = null,

    var workType: String? = null,
    var model: String? = null,
    var technicType: String? = null,

    var productionYear: Int? = null,
    var engineNumber: String? = null,
    var enginePower: Int? = null,

    var chassisNumber: String? = null,
    var color: String? = null,

    var regionCode: String? = null,
    var series: String? = null,
    var number: String? = null

) : BaseEntity()