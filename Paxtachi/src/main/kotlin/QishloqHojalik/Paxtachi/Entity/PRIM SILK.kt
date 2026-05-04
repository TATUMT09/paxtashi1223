package QishloqHojalik.Paxtachi.Entity

import jakarta.persistence.*

@Entity
@Table(name = "greenhouse_records")
class GreenhouseRecord(

    var ownerName: String? = null,
    var totalArea: Double? = null,
    var contourNumber: String? = null,

    var greenhouseArea: Double? = null,
    var cropName: String? = null,
    var passportSeries: String? = null,

    var jshshir: String? = null,
    var mfy: String? = null,

    var seedlingCount: Int? = null,
    var requiredSeedlingCount: Int? = null,

    @Column(columnDefinition = "TEXT")
    var locationUrl: String? = null,

    var phoneNumber: String? = null

) : BaseEntity()