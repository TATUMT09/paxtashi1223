package QishloqHojalik.Paxtachi.Enums

enum class Direction(
    val panelUrl: String
) {

    PAXTACHILIK("/dashboard"),

    TUTCHILIK("/dashboard"),

    GALLACHILIK("/gallachilik"),

    BOGDORCHILIK("/bogdorchilik"),

    CHORVACHILIK("/farm-representatives"),

    PUMP("/pump"),

    AGRO_TECHNIC("/agro-technics"),

    CADASTRE("/cadastre-land-records"),

    GREENHOUSE("/greenhouse-records"),

    FARM_REPRESENTATIVE("/farm-representatives"),

    LAND_CONTOUR("/land-contours"),

    FARMERS("/farmers"),

    DASHBOARD("/dashboard"),

    SPECIALIZATION("/specializations"),

    LOOKUP("/lookups"),

    SUPER_ADMIN("/dashboard"),

    ADMIN("/dashboard"),

    USER("/dashboard"),

    HOKIM("/hokim")
}
enum class Role {
    SUPER_ADMIN,
    ADMIN,
    USER
}

