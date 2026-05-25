package QishloqHojalik.Paxtachi.Enums

enum class Direction(
    val panelUrl: String
) {

    PAXTACHILIK("/paxtachilik"),

    TUTCHILIK("/tutchilik"),

    GALLACHILIK("/gallachilik"),

    BOGDORCHILIK("/bogdorchilik"),

    CHORVACHILIK("/chorvachilik"),

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

    SUPER_ADMIN("/super-admin"),

    ADMIN("/admin"),

    USER("/user")
}
enum class Role {
    SUPER_ADMIN,
    ADMIN,
    USER
}

