package QishloqHojalik.Paxtachi.Entity

import QishloqHojalik.Paxtachi.Enums.Direction
import QishloqHojalik.Paxtachi.Enums.Specialization
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Entity
@Table(name = "paxtachi_balance")
class PaxtachiBalance(

    var orderNumber: Int? = null,
    var name: String? = null,
    var farmType: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    var specialization: Specialization? = null,

    var rightType: String? = null,
    var stir: String? = null,
    var cadastralNumber: String? = null,
    var totalArea: Double? = null,
    var irrigatedArea: Double? = null,
    var cottonArea: Double? = null,
    var wheatArea: Double? = null,
    var greenhouseArea: Double? = null,
    var lalmiArea: Double? = null,
    var orchardArea: Double? = null,
    var grapeArea: Double? = null,
    var poplarArea: Double? = null,
    var otherTreeArea: Double? = null,
    var pastureArea: Double? = null,
    var hayfieldArea: Double? = null,
    var meliorationArea: Double? = null,
    var pondArea: Double? = null,
    var reservoirArea: Double? = null,
    var canalArea: Double? = null,
    var houseArea: Double? = null,
    var yardArea: Double? = null,
    var buildingArea: Double? = null,
    var otherArea: Double? = null,
    var totalBalance: Double? = null,
    var season: Int? = null

) : BaseEntity()