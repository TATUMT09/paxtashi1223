package QishloqHojalik.Paxtachi.Entity


import jakarta.persistence.*

@Entity
@Table(name = "cadastre_land_records")
class CadastreLandRecord(

    var regionName: String? = null,        // Viloyat nomi
    var districtName: String? = null,      // Tuman nomi
    var neighborhoodName: String? = null,  // Mahalla nomi

    var contourNumber: String? = null,     // Гуручистон рақами / kontur raqami
    var area: Double? = null,              // Maydoni (ga)

    var landType: String? = null,          // Ekin / yer turi
    var farmName: String? = null,          // Xo‘jalik / fermer nomi

    var landNumber: String? = null,        // Yer raqami
    var cadastreNumber: String? = null,    // Kadastr raqami

    var cadastralRegistered: Int? = null,  // kadastr ro‘yxatidan o‘tgan
    var notRegisteredReason: String? = null,

    var problem: String? = null,           // Muammoli
    var problemDescription: String? = null // Muammo sababi

) : BaseEntity()